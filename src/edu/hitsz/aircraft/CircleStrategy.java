package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;

public class CircleStrategy implements Stratege{
    private static final int shootNum = 20;
    public LinkedList<BaseBullet> makeBullets(int x,int y,int distance, int speed, int power,AbstractAircraft aircraft){
        LinkedList<BaseBullet> res = new LinkedList<>();
        BaseBullet bullet;

        for(int i=0; i<shootNum; i++){
            double angleDegrees = i * (360.0 / shootNum);
            double angleRadians = Math.toRadians(angleDegrees);
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            if(aircraft instanceof Enemy){
                bullet = new EnemyBullet(x + (int)(distance*Math.cos(angleRadians)), y + (int)(distance*Math.sin(angleRadians)), (int)(speed*Math.sin(angleRadians)), (int)(speed*Math.cos(angleRadians)), power);
            }
            else bullet = new HeroBullet(x + (int)(distance*Math.cos(angleRadians)), y + (int)(distance*Math.sin(angleRadians)), (int)(speed*Math.sin(angleRadians)), (int)(speed*Math.cos(angleRadians)), power);
            res.add(bullet);
        }
        return res;
    }
}
