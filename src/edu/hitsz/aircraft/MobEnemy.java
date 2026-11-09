package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public class MobEnemy extends Enemy {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    private int direction = 1;
    private int power = 30;
    @Override
    public List<BaseBullet> shoot(){
        return this.shootStratege.makeBullets(this.locationX,this.locationY,2 * direction,this.getSpeedY()+5*direction,power,this);
    }

}
