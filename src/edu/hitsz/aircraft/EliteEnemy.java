package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends Enemy{
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    private int direction = 1;
    private int power = 30;
    @Override
    public List<BaseBullet> shoot(){
        this.setShootStratege(new SingleStrategy());
        return this.shootStratege.makeBullets(this.locationX,this.locationY,2 * direction,this.getSpeedY()+5*direction,power,this);
    }
}
