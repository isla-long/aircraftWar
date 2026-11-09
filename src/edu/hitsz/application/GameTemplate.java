package edu.hitsz.application;

import javax.swing.*;
import java.util.TimerTask;
import java.util.Timer;

public abstract class GameTemplate extends JPanel {
    protected final Timer timer;
    protected final int timeInterval = 40;
    public GameTemplate(){
        this.timer = new java.util.Timer("game-action-timer", true);
    }
    public final void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //添加观察者
                observerAddAction();
                //敌机生成逻辑
                enemyCreateAction();
                // 子弹道具持续时间减少
                timeAction();
                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 播放音乐
                musicPlay();
                // 飞机移动
                aircraftsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task,0,timeInterval);

    }

    protected abstract void observerAddAction();
    protected abstract void enemyCreateAction();
    protected abstract void timeAction();
    protected abstract void shootAction();
    protected abstract void bulletsMoveAction();
    protected abstract void musicPlay();
    protected abstract void aircraftsMoveAction();
    protected abstract void crashCheckAction();
    protected abstract void postProcessAction();
    protected abstract void checkResultAction();

}
