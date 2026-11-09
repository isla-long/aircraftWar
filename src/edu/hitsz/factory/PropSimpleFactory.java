package edu.hitsz.factory;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;

public  class PropSimpleFactory {
    public static AbstractFlyingObject createProp(int locationX,int locationY,int speedX,int speedY,int type){
        AbstractFlyingObject prop;
        switch (type){
            case 1:prop = new prop_blood(locationX,locationY,speedX,speedY);break;
            case 2:prop = new prop_bomb(locationX,locationY,speedX,speedY);break;
            case 3:prop = new prop_bullet(locationX,locationY,speedX,speedY);break;
            case 4:prop = new prop_bullet_plus(locationX,locationY,speedX,speedY);break;
            case 5:prop = new prop_freeze(locationX,locationY,speedX,speedY);break;
            default:prop = null;
        }
        return prop;
    }
}
