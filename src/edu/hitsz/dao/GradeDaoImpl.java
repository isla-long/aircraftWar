package edu.hitsz.dao;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class GradeDaoImpl implements GradeDao{
    private static String PATH;

    private  List<Map<String,Object>> grades = new LinkedList<>();

    public GradeDaoImpl(int difficulty){
        PATH = "src/edu/hitsz/dao/grade_" + difficulty;
    }

    @Override
    public List<Map<String, Object>> findById(String id) {
        List<Map<String,Object>> res = new LinkedList<>();
        for(Map<String,Object> map : grades){
            if(Objects.equals((String)map.get("ID"),id)){
                Map<String,Object> copy = new HashMap<>(map);
                res.add(copy);
            }
        }
        if(res.isEmpty()) return null;
        else return res;
    }

    @Override
    public List<Map<String, Object>> getAllGrades() {
        return new LinkedList<>(grades);
        //return grades;
    }

    @Override
    public void addGrade(String id, int grade, String time) {
        Map<String, Object> map = new HashMap<>();
        map.put("ID",id);
        map.put("Grade",grade);
        map.put("Time" , time);
        grades.add(map);
        gradesSort();
    }

    @Override
    public void deleteGrade(String id) {
        grades.removeIf(map -> Objects.equals((String) map.get("ID"), id));
        gradesSort();
    }

    private void gradesSort() {
        grades.sort((Map<String,Object> map1,Map<String,Object> map2)->{
            return Integer.compare((int) map2.get("Grade"), (int) map1.get("Grade"));
        });
    }

    public void printAllGrades(){
        int rank = 0;
        for(Map<String,Object> map : grades){
            rank ++;
            String res ="Rank: "+rank+" ,ID: " + (String) map.get("ID") + " ,Grade:" + (int) map.get("Grade") + " , Time:" + map.get("Time");
            System.out.println(res);
        }
    }

    public void writeGradeFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))){
            int rank = 0;
            for(Map<String,Object> map : grades){
                rank ++;
                String input = "Rank: "+rank+" ,ID: " + (String) map.get("ID") + " ,Grade:" + (int) map.get("Grade") + " , Time:" + map.get("Time");
                writer.write(input);
                writer.newLine();
            }
            writer.flush();
        }
        catch (IOException e){
            System.out.print("fail to write the grades list to file !!");
        }
    }

    public void loadGradeFile(){
        grades.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader(PATH))){
            String line;
            while((line = reader.readLine()) != null){
                line = line.replace("Rank:","").replace("ID:","").replace("Grade:","").replace("Time:","");
                String id;int grade;String time;int rank;
                String[] info = new String[4];
                info = line.trim().split(",");
                id = info[1].trim();
                grade = Integer.parseInt(info[2].trim());
                time = info[3].trim();
                addGrade(id,grade,time);
            }
        }
        catch (IOException e){
            System.out.print("fail to load grade file to the grades list!!");
        }
    }

    public void removeAllGrades(){
        grades.clear();
        loadGradeFile();
    }
}
