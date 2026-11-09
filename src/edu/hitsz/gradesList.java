package edu.hitsz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class gradesList {
    private JTable gradeslist;
    private JPanel gradespane;
    private JLabel gradeshead;
    private JScrollPane gradeslistpane;
    public gradesList(List<Map<String,Object>> grades){
        String[] datahead = {"排名" , "用户名" , "成绩" , "时间"};
        List<String[]> data = new LinkedList<>();
        int i = 0;
        for(Map<String,Object> map : grades ){
            i++;
            String[] buf = new String[4];
            buf[0] = i + "";
            buf[1] = (String) map.get("ID");
            buf[2] =  (int)map.get("Grade") + "";
            buf[3] = (String) map.get("Time");
            data.add(buf);
        }

        String[][] dataarray = data.toArray(new String[0][]);
        DefaultTableModel model = new DefaultTableModel(dataarray, datahead){
          @Override
          public boolean isCellEditable(int row , int col){
              return false;
          }
        };

        gradeslist.setModel(model);
        TableColumnModel columnModel = gradeslist.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(0).setMinWidth(30);
        columnModel.getColumn(0).setMaxWidth(70);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(2).setMinWidth(80);
        columnModel.getColumn(2).setMaxWidth(120);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(1).setMinWidth(150);
        columnModel.getColumn(1).setMaxWidth(50);
        gradeslistpane.setViewportView(gradeslist);

    }

    public JPanel getGradespane(){return gradespane;}
}
