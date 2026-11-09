package edu.hitsz.observer;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.Enemy;
import edu.hitsz.aircraft.prop;
import edu.hitsz.application.Game;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.List;

public interface observer {

    public void onBomb(Game game);

    public void onFreeze(Game game);

}
