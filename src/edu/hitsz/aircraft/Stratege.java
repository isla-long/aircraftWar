package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;

public interface Stratege {
    LinkedList<BaseBullet> makeBullets(int x,int y,int distance,int speed,int power,AbstractAircraft aircraft);
}
