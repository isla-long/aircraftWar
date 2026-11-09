package edu.hitsz.dao;

import java.util.List;
import java.util.Map;

public interface GradeDao {
    public List<Map<String,Object>> findById(String id);

    public List<Map<String,Object>> getAllGrades();

    public void addGrade(String id,int grade , String time);

    public void deleteGrade(String id);
}
