package edu.hitsz.player;

import javax.sound.sampled.*;
import java.io.*;
import java.rmi.server.ObjID;

public class musicPlayerThread extends Thread{
    private String Path;
    private boolean isPaused = true;
    private Object lock = new Object();
    private boolean isloop = false;

    public musicPlayerThread(String path , boolean isloop){
        this.Path = path;
        this.isloop = isloop;
    }
    @Override
    public void run(){
        while(true){
            play_one_time();
            if(!isloop){
                break;
                //stopPlaying();
            }
        }
        }

    public void stopPlaying(){
        boolean _isPaused = true;
        if(isPaused != _isPaused){
            synchronized (lock){
                lock.notify();
            }
        }
        isPaused = _isPaused;
    }

    public void beginPlaying(){
        boolean _isPaused = false;
        if(isPaused != _isPaused){
            synchronized (lock){
                lock.notify();
            }
        }
        isPaused = _isPaused;
    }

    public void play_one_time(){
        try{
            File file = new File(Path);
            AudioInputStream music= AudioSystem.getAudioInputStream(file);
            AudioFormat format = music.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class , format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            line.open(format);
            line.start();

            byte[] buffer = new byte[1024*4];
            int streamread;
            while((streamread = music.read(buffer)) != -1){
                synchronized (lock){
                    while(isPaused){
                        line.stop();
                        lock.wait();
                        line.start();
                    }
                }
                line.write(buffer,0,streamread);
            }
            line.drain();
            line.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        musicThreadManager.bgMusicPlayer.start();
        musicThreadManager.bgMusicPlayer.beginPlaying();
    }
}
