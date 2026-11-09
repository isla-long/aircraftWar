package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.MobEnemy;

public class ElitePlusFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp){
        return new ElitePlusEnemy( locationX, locationY, speedX, speedY, hp);
    }
}
