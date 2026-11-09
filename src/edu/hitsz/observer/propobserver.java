package edu.hitsz.observer;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.Game;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.PropSimpleFactory;

import java.util.ArrayList;
import java.util.List;

public class propobserver implements observer{



    @Override
    public void onBomb(Game game) {
        List<AbstractAircraft> enemyList = game.enemyAircrafts;
        List<BaseBullet> bulletList = game.enemyBullets;
        List<AbstractFlyingObject> props = game.propToAdd;
        for(AbstractAircraft enemyAircraft : enemyList){
            if(enemyAircraft instanceof MobEnemy)enemyAircraft.vanish();
            if(enemyAircraft instanceof EliteEnemy)enemyAircraft.vanish();
            if(enemyAircraft instanceof EliteProEnemy)enemyAircraft.vanish();
            if(enemyAircraft instanceof ElitePlusEnemy)enemyAircraft.decreaseHp(20);
            if(enemyAircraft instanceof BossEnemy){}
            if (enemyAircraft.notValid()) {
                int select = (int) (Math.random()*5+1);
                if(!(enemyAircraft instanceof MobEnemy) && !(enemyAircraft instanceof BossEnemy)){
                    AbstractFlyingObject prop = PropSimpleFactory.createProp(enemyAircraft.getLocationX(),enemyAircraft.getLocationY(),0,5,select);
                    if(prop != null && ((enemyAircraft instanceof ElitePlusEnemy) && !(prop instanceof prop_freeze))){
                        props.add(prop);
                    }
                }
                game.score += 10;
            }
        }
        for(BaseBullet bullet : bulletList){
            bullet.vanish();
        }
    }

    @Override
    public void onFreeze(Game game) {
        List<AbstractAircraft> enemyList = game.enemyAircrafts;
        List<BaseBullet> bulletList = game.enemyBullets;
        for(AbstractAircraft e : enemyList){
            if(e instanceof MobEnemy)e.setFreezeTime(Integer.MAX_VALUE);
            if(e instanceof EliteEnemy)e.setFreezeTime(4 * 25);
            if(e instanceof EliteProEnemy)e.setFreezeTime(3 * 25);
            if(e instanceof ElitePlusEnemy)e.setFreezeTime(5 * 25);
            if(e instanceof BossEnemy){}
        }
        for(BaseBullet bullet : bulletList){
            bullet.setFreezeTime(5 * 25);
        }
    }


}
