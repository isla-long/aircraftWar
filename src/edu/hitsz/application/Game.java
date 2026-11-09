package edu.hitsz.application;

import com.sun.jdi.connect.Connector;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.dao.GradeDaoImpl;
import edu.hitsz.factory.*;
import edu.hitsz.gradesList;
import edu.hitsz.observer.propobserver;
import edu.hitsz.player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends GameTemplate {
    private static final String ConfigPath = "src/edu/hitsz/application/difficulty-config";
    private boolean allowBoss = true;
    private int bossNumber = 0;
    private boolean allowBossHpUp = false;
    private boolean mainMusicFlag = false;
    private boolean bossMusicFlag = false;
    private propobserver obs  = new propobserver();
    private static int count = 0;
    //播放器
    private musicPlayerThread bgplayer = musicThreadManager.bgMusicPlayer;
    private musicPlayerThread bossplayer = musicThreadManager.bossMusicPlayer;
    private musicPlayerThread bombplayer = musicThreadManager.bossMusicPlayer;
    private musicPlayerThread gameoverplayer = musicThreadManager.gameOverMusicPlayer;
    public List<AbstractFlyingObject> propToAdd = new ArrayList<>();
    private int difficulty = 0;
    private int prop_duration = 0;
    private int timetick = 0;
    private int backGroundTop = 0;
    //调度器, 用于定时任务调度

    private final HeroAircraft heroAircraft;
    public final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    public final List<BaseBullet> enemyBullets;
    public final List<AbstractFlyingObject> props;
    //屏幕中出现的敌机最大数量
    private int enemyMaxNumber = 5;
    //boss生成阈值
    private int bossPoint = 200;
    //敌机速度
    private int enemySpeedY = 10;
    private double dEnemySpeedY = 0.008;
    //精英敌机血量
    private int eliteEnemyHp = 30;
    private double dEliteEnemyHp = 0.04;
    //敌机生成概率
    private double[] probability = {0.6 , 0.2, 0.15 ,0.05};
    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    protected double dEnemySpawnCycle = 0.04;
    private int enemySpawnCounter = 0;

    //英雄机和敌机射击周期
    protected double shootCycle = 20;
    protected double dShootCycle = 0.04;
    private int shootCounter = 0;
    private String username;
    //当前玩家分数
    public int score = 0;

    //游戏结束标志
    private boolean gameOverFlag = false;

    public void readconfig(){
        try(BufferedReader reader = new BufferedReader(new FileReader(ConfigPath))){
            String line;
            reader.readLine();
            while((line = reader.readLine())!=null){
                String[] info = new String[6];
                info = line.trim().split(",");
                if(difficulty == Integer.parseInt(info[0].trim())) {
                    enemyMaxNumber = Integer.parseInt(info[1].trim());
                    probability = Arrays.stream(info[2].split("-"))
                            .mapToDouble(s -> Double.parseDouble(s.trim()))
                            .toArray();
                    shootCycle = Double.parseDouble(info[3].trim());
                    enemySpawnCycle = Double.parseDouble(info[4].trim());
                    bossPoint = Integer.parseInt(info[5].trim());
                    System.out.println("successfully download config file(difficulty : " + difficulty +")");
                    System.out.println("shootCycle : " + shootCycle);
                }
            }
        }
        catch (IOException e){
            System.out.println("failed to open config file");
            e.printStackTrace();
        }
    }

    public boolean BossExisting(){
        int bossNum = 0;
        for(AbstractAircraft e : enemyAircrafts){
            if(e instanceof BossEnemy)bossNum = 1;
        }
        if(bossNum == 0 && (this.score % bossPoint == 0 && this.score != 0)){
            return true;
        }
        return false;
    }
    public Game(int setdifficulty,String name) {
        super();
        count += 1;
        System.out.println("第" + count + "次调用new game()");
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        this.difficulty = setdifficulty;
        readconfig();
        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);
        if(difficulty == 1)allowBoss = false;
        if(difficulty >= 3)allowBossHpUp = true;
        username = name;
    }


    //***********************
    //      Action 各部分
    //***********************
    protected void observerAddAction(){
        for(AbstractFlyingObject p : props){
            if(p instanceof prop_bomb || p instanceof  prop_freeze){
                if(!(((prop) p).propobserverList.contains(obs))) ((prop) p).addobs(obs);
            }
        }
    }
    protected void timeAction(){
        heroAircraft.decreaseBullet_duration(1);
        for(AbstractAircraft e : enemyAircrafts){
            e.decreaseFreezeTime(1);
        }
        for(BaseBullet bullet : enemyBullets){
            bullet.decreaseFreezeTime(1);
        }
        if(difficulty == 2){
            if(enemySpawnCycle >= 8 ) enemySpawnCycle -= dEnemySpawnCycle;
            enemySpeedY = (int)(enemySpeedY + dEnemySpeedY);
            eliteEnemyHp = (int)(eliteEnemyHp + dEliteEnemyHp);
        }
        else if(difficulty == 3){
            if(enemySpawnCycle >= 8 ) enemySpawnCycle -= dEnemySpawnCycle;
            enemySpeedY = (int)(enemySpeedY + dEnemySpeedY);
            eliteEnemyHp = (int)(eliteEnemyHp + dEliteEnemyHp);
            if(shootCycle >= 8) shootCycle -= dShootCycle;
        }

    }


    protected void enemyCreateAction(){
        enemySpawnCounter++;
        if (enemySpawnCounter >=enemySpawnCycle) {
            enemySpawnCounter = 0;
            // 产生普通敌机
            if(BossExisting() && allowBoss){
                EnemyFactory factory;
                factory = new BossEnemyFactory();
                enemyAircrafts.add(factory.createAircraft((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                        (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                        5,
                        0,
                        200 + 100*bossNumber));
                if(allowBossHpUp) bossNumber++;
            }

            if (enemyAircrafts.size() < enemyMaxNumber) {
                double rand = Math.random();
                int eliteSpeedX = (int)(Math.random() * 7 - 3);
                EnemyFactory factory;

                if (rand < probability[0]) {
                    factory = new MobEnemyFactory();
                    enemyAircrafts.add(factory.createAircraft(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                            0,
                            enemySpeedY,
                            30
                    ));
                }
                else if (rand < probability[0] + probability[1]) {
                    factory = new EliteEnemyFactory();
                    enemyAircrafts.add(factory.createAircraft(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                            0,
                            enemySpeedY,
                            eliteEnemyHp
                    ));
                }
                else if (rand < probability[0] + probability[1] + probability[2]) {
                    factory = new ElitePlusFactory();
                    enemyAircrafts.add(factory.createAircraft(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                            eliteSpeedX,
                            enemySpeedY,
                            eliteEnemyHp
                    ));
                }
                else {
                    factory = new EliteProFactory();
                    enemyAircrafts.add(factory.createAircraft(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PRO_IMAGE.getWidth())),
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05),
                            eliteSpeedX,
                            enemySpeedY,
                            eliteEnemyHp
                    ));
                }
            }
        }

    }
    protected void musicPlay(){
        if(!mainMusicFlag && !BossExisting()){
            bgplayer.beginPlaying();
            mainMusicFlag = true;
            if(bossMusicFlag){
                bossplayer.stopPlaying();
                bossMusicFlag = false;
            }
        }
        else if(BossExisting() && mainMusicFlag){
            if(!bossMusicFlag){
                bossplayer.beginPlaying();
                bossMusicFlag = true;
            }
            bgplayer.stopPlaying();
            mainMusicFlag = false;
        }

    }

    protected void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());
            // TODO 敌机射击
            for(AbstractAircraft e : enemyAircrafts){
                enemyBullets.addAll(e.shoot());
            }
        }
    }

    protected void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    protected void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        for (AbstractFlyingObject prop : props){
            prop.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    protected void crashCheckAction() {


        // TODO 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets){
            if(bullet.notValid()){continue;}
            if(heroAircraft.crash(bullet)){
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    musicPlayerThread bulletmusic = musicThreadManager.getNewBulletHitMusicPlayer();
                    bulletmusic.start();
                    bulletmusic.beginPlaying();

                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        int select = (int) (Math.random()*5+1);
                        if(!(enemyAircraft instanceof MobEnemy) && !(enemyAircraft instanceof BossEnemy)){
                            AbstractFlyingObject prop = PropSimpleFactory.createProp(enemyAircraft.getLocationX(),enemyAircraft.getLocationY(),0,5,select);
                            if(prop != null && ((enemyAircraft instanceof ElitePlusEnemy) && !(prop instanceof prop_freeze))){
                                props.add(prop);
                            }
                        }
                        else if(enemyAircraft instanceof BossEnemy){
                            int bossselect1 = (int) (Math.random()*5 + 1);
                            int bossselect2= (int) (Math.random()*5 + 1);
                            int bossselect3 = (int) (Math.random()*5 + 1);
                            AbstractFlyingObject prop1 = PropSimpleFactory.createProp(enemyAircraft.getLocationX(),enemyAircraft.getLocationY()+50,0,5,bossselect1);
                            AbstractFlyingObject prop2 = PropSimpleFactory.createProp(enemyAircraft.getLocationX()+10,enemyAircraft.getLocationY()+50,0,5,bossselect2);
                            AbstractFlyingObject prop3 = PropSimpleFactory.createProp(enemyAircraft.getLocationX()+20,enemyAircraft.getLocationY()+50,0,5,bossselect3);
                            props.add(prop1);
                            props.add(prop2);
                            props.add(prop3);
                        }
                        score += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for(AbstractFlyingObject prop : props){
            if(prop.notValid()) continue;
            if(prop.crash(heroAircraft)){
                prop.be_used(heroAircraft,this);
                if(prop instanceof prop_bullet || prop instanceof prop_bullet_plus){
                    heroAircraft.setBullet_duration(250);
                }
                prop.vanish();

                musicPlayerThread propmusic = musicThreadManager.getNewGetSupplyMusicPlayer();
                propmusic.start();
                propmusic.beginPlaying();
            }
        }
        props.addAll(propToAdd);
        propToAdd.clear();
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    protected void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        // Todo: 删除无效道具
        props.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    protected void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 取消定时器并终止所有调度任务
            gameOverFlag = true;
            System.out.println("Game Over!");

            // 处理时间数据
            LocalDateTime time_ = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time = time_.format(formatter);

            // 存入数据并打印排行榜
            GradeDaoImpl dao = new GradeDaoImpl(difficulty);
            dao.loadGradeFile();
            dao.addGrade(username,this.score,time);
            dao.printAllGrades();
            dao.writeGradeFile();

            // 播放战败CG
            gameoverplayer.beginPlaying();

            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new gradesList(dao.getAllGrades()).getGradespane());
            frame.setBounds((int) screensize.getWidth()/2-100,(int) screensize.getHeight()/2,300,500);
            frame.pack();
            frame.setResizable(true);
            frame.setVisible(true);


        }
    };

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        switch (difficulty){
            case 1:
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG, 0, this.backGroundTop, null);
                break;
            case 2:
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG2, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG2, 0, this.backGroundTop, null);
                break;
            case 3:
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG3, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG3, 0, this.backGroundTop, null);
                break;
            case 4:
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG4, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG4, 0, this.backGroundTop, null);
                break;
            case 5:
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG5, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
                g.drawImage(ImageManager.BACKGROUND_IMAGE_BG5, 0, this.backGroundTop, null);
                break;

        }
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        // Todo: 绘制道具
        paintImageWithPositionRevised(g,props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

}
