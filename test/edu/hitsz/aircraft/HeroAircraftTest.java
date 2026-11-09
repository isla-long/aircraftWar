package edu.hitsz.aircraft;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {
    private HeroAircraft hero;
    @BeforeEach
    void begin(){
        hero = HeroAircraft.getInstance();
        hero.reset();
    }

    @DisplayName("测试英雄机初始血量以及各种情况下血量变化是否正确")
    @ParameterizedTest
    @CsvSource({"20 , 50", "50 , 0" , "200 , 50"})
    void hpChangeTest(int dhp , int ihp) {
        assertEquals(200,hero.getHp(),"初始血量不是200");
        int initHp = 200;
        hero.decreaseHp(dhp);
        assertEquals(Math.max(initHp - dhp , 0) , hero.getHp(),"血量扣除有误");
        hero.increaseHp(ihp);
        assertEquals(Math.min(initHp - dhp + ihp, 200) , hero.getHp() , "血量增加有误");
        System.out.println("血量测试通过");
    }

    @DisplayName("碰撞测试")
    @Test
    void crashTest() {
        MobEnemy enemy = new MobEnemy(hero.getLocationX(), hero.getLocationY(), 0 , 0 ,30);
        assertTrue(hero.crash(enemy),"碰撞测试失败");
        System.out.println("碰撞测试成功");
    }

    @DisplayName("子弹生成测试")
    @Test
    void bulletsTest(){
        List<BaseBullet> res = hero.shoot();
        assertNotNull(res , "没有生成子弹");
        for(BaseBullet bullet : res){
            assertEquals(30 , bullet.getPower(),"子弹威力不为30");
        }
        System.out.println("子弹生成测试成功");
    }

}