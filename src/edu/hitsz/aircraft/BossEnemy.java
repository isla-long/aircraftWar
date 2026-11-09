package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends  Enemy{
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setShootStratege(new CircleStrategy());
    }
    private int direction = 1;
    private int power = 30;
    @Override
    public List<BaseBullet> shoot(){
        return this.shootStratege.makeBullets(this.locationX, this.locationY, 2 * direction, 10 * direction, power,this);    }
}
