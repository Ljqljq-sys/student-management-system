package com.ljq.sms.service;

import com.ljq.sms.entity.Student;
import java.util.List;

public interface StudentService {
    /** 添加学生（学号重复会抛 DuplicateIdException） */
    void addStudent(Student student);

    /** 按学号查询（查不到会抛异常） */
    Student getStudent(String id);

    /** 查询全部 */
    List<Student> getAllStudents();

    /** 修改学生（学生不存在会抛异常） */
    void updateStudent(Student student);

    /** 删除学生（学生不存在会抛异常） */
    void deleteStudent(String id);

}
