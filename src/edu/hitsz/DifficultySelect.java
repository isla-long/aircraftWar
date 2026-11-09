package edu.hitsz;

import edu.hitsz.application.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultySelect {
    private int difficulty = 0;
    private JLabel DifficultyCoefficient;
    private JTextField textField;
    private JPanel DifficultySelectPanel;
    private JButton ConfirmButton;
    private JTextField userTextField;
    private JLabel name;

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public DifficultySelect(JFrame firstframe){
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField.getText();
                String name = userTextField.getText();
                if(s1.isEmpty()){
                    JOptionPane.showMessageDialog(null,"输入不能为空","提示",JOptionPane.ERROR_MESSAGE);
                }
                difficulty = Integer.parseInt(s1);
                if(difficulty < 1 || difficulty > 5) {
                    JOptionPane.showMessageDialog(null, "难度系数在0到5之间", "错误", JOptionPane.WARNING_MESSAGE);
                }

                // 开始游戏
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                JFrame frame = new JFrame("Aircraft War");
                frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                frame.setResizable(false);
                //设置窗口的大小和位置,居中放置
                frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                        WINDOW_WIDTH, WINDOW_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Game game = new Game(difficulty,name);
                frame.add(game);
                frame.setVisible(true);
                game.action();

                // 关闭
                firstframe.dispose();
            }
        });
    }
    public JPanel getMainPanel(){
        return DifficultySelectPanel;
    }

}
