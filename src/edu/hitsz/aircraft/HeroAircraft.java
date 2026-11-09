package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    private int bullet_duration = 0;

    private static   HeroAircraft   instance = new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 1000);
    //每次射击发射子弹数量

    //子弹威力
    private int power = 30;

    //子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setShootStratege(new SingleStrategy());
    }

    public static HeroAircraft getInstance(){
        return instance;
    }

    public void decreaseBullet_duration(int step){
        this.bullet_duration = (bullet_duration < step)? 0 : (bullet_duration - step);
        if(bullet_duration == 0){
            this.setShootStratege(new SingleStrategy());
        }
    }

    public void setBullet_duration(int bullet_duration){
        this.bullet_duration = bullet_duration;
    }
    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        return this.shootStratege.makeBullets(this.locationX, this.locationY, 2 * direction, 10 * direction, power,this);
    }

    public void reset(){
        this.hp = maxHp;
    }
}
