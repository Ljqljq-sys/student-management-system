package com.ljq.sms.dao;

import com.ljq.sms.entity.Student;
import java.util.List;

public interface StudentDao {
    //添加学生
    void add(Student student);
    //按学号查询
    Student findById(String id);
    //查询全部学生
    List<Student> findAll();
    //更新学生信息
    void update(Student student);
    //按学号删除
    void delete(String id);
    //判断学号是否存在
    boolean existsById(String id);
}
