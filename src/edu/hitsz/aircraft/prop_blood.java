package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;

import java.util.List;

public class prop_blood extends prop {
    @Override
    public void notifyobservers(Game game) {

    }

    public prop_blood(int locationX, int locationY, int speedX, int speedY){
        super(locationX,locationY,speedX,speedY);
    }

    @Override
    public void be_used(AbstractAircraft aircraft,Game game){
        aircraft.increaseHp(200);
    }
}
