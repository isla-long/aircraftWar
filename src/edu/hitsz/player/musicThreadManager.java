package edu.hitsz.player;

import java.util.LinkedList;
import java.util.List;

public class musicThreadManager {
    public static List<musicPlayerThread> list = new LinkedList<>();

    public static String PATH_BG = "src/videos/bgm.wav";
    public static String PATH_BOSS = "src/videos/bgm_boss.wav";
    public static String PATH_BOMB_EXPLOSION = "src/videos/bgm.wav";
    public static String PATH_BULLET_HIT = "src/videos/bomb_explosion.wav";
    public static String PATH_GAME_OVER = "src/videos/game_over.wav";
    public static String PATH_GET_SUPPLY = "src/videos/get_supply.wav";

    public static musicPlayerThread bgMusicPlayer = new musicPlayerThread(PATH_BG,true);
    public static musicPlayerThread bossMusicPlayer = new musicPlayerThread(PATH_BOSS,true);
    //public static musicPlayerThread bombMusicPlayer = new musicPlayerThread(PATH_BOMB_EXPLOSION,false);
    public static musicPlayerThread gameOverMusicPlayer = new musicPlayerThread(PATH_GAME_OVER,false);
    //public static musicPlayerThread bulletHitMusicPlayer = new musicPlayerThread(PATH_BULLET_HIT,false);
    //public static musicPlayerThread getSupplyMusicPlayer = new musicPlayerThread(PATH_GET_SUPPLY,false);

    public static musicPlayerThread getNewBombMusicPlayer(){
        musicPlayerThread thread = new musicPlayerThread(PATH_BOMB_EXPLOSION,false);
        list.add(thread);
        return thread;
    }
    public static musicPlayerThread getNewBulletHitMusicPlayer(){
        musicPlayerThread thread = new musicPlayerThread(PATH_BULLET_HIT,false);
        list.add(thread);
        return thread;
    }


    public static musicPlayerThread getNewGetSupplyMusicPlayer(){
        musicPlayerThread thread = new musicPlayerThread(PATH_GET_SUPPLY,false);
        list.add(thread);
        return thread;
    }

    public static void stopAllThread(){
        for(musicPlayerThread thread : list){
            thread.stopPlaying();
        }
    }
    static{
        //单线程处理
        list.add(bgMusicPlayer);
        list.add(bossMusicPlayer);
        list.add(gameOverMusicPlayer);
        bgMusicPlayer.start();
        bossMusicPlayer.start();
        gameOverMusicPlayer.start();
    }
}
