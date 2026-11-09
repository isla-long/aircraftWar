package edu.hitsz.application;

import edu.hitsz.DifficultySelect;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame

        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("DifficultySelect");
        frame.setContentPane(new DifficultySelect(frame).getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds((int)screensize.getWidth()/2-100,(int)screensize.getHeight()/2,200,200);
        frame.pack();
        frame.setVisible(true);
    }
}
