package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.observer.observer;
import edu.hitsz.observer.propobserver;

import java.util.ArrayList;
import java.util.List;

public abstract class prop extends AbstractFlyingObject {
    public List<propobserver> propobserverList = new ArrayList<>();
    public void addobs(propobserver obs){
        propobserverList.add(obs);
    }
    private void removeobs(propobserver obs){
        if(propobserverList.contains(obs)){
            propobserverList.remove(obs);
        }
        else return;
    }
    public abstract void notifyobservers(Game game);
    public prop(int locationX,int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }
    public abstract void be_used(AbstractAircraft aircraft,Game game);
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }
}
