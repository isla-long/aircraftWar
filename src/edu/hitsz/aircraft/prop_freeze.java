package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.observer.propobserver;

import java.util.List;

public class prop_freeze extends prop {
    @Override
    public void notifyobservers(Game game) {
        for(propobserver obs : propobserverList){
            obs.onFreeze(game);
        }
    }

    public prop_freeze(int locationX, int locationY, int speedX, int speedY){
        super(locationX,locationY,speedX,speedY);
    }

    public void be_used(AbstractAircraft aircraft,Game game){
        System.out.println("prop_freeze has been used");
        notifyobservers(game);
    }
}
