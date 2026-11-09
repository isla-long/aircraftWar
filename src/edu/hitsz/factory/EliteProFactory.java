package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteProEnemy;
import edu.hitsz.aircraft.MobEnemy;

public class EliteProFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp){
        return new EliteProEnemy( locationX, locationY, speedX, speedY, hp);
    }
}
