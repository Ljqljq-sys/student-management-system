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

    /** 按姓名模糊搜索 */
    List<Student> searchByName(String keyword);

    /** 按成绩排序（ascending=true 升序，false 降序） */
    List<Student> sortByScore(boolean ascending);

    /** 获取统计信息：平均分、最高分、最低分、及格率 */
    String getStatistics();
}
