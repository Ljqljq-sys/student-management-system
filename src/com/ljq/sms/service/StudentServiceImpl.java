package com.ljq.sms.service;

import com.ljq.sms.dao.StudentDao;
import com.ljq.sms.dao.StudentDaoFileImpl;
import com.ljq.sms.entity.Student;
import com.ljq.sms.exception.DuplicateIdException;
import com.ljq.sms.exception.StudentNotFoundException;

import java.util.List;

/**
 * 学生业务逻辑实现
 * 负责业务规则：学号不能重复、学生必须存在
 */
public class StudentServiceImpl implements StudentService {

    // Service 持有 Dao，通过它访问数据（这叫"组合"）
    private final StudentDao studentDao = new StudentDaoFileImpl();
    //组合**（Composition）—— 一个对象"拥有"另一个对象。
    //StudentServiceImpl 有一个 StudentDao

    @Override
    public void addStudent(Student student) {
        // 业务规则 1：学号不能重复
        //DAO 的 add 是"无脑存"；Service 的 addStudent 是"先检查，再存"。
        if (studentDao.existsById(student.getId())) {
            throw new DuplicateIdException("学号 " + student.getId() + " 已存在");
        }
        studentDao.add(student);
    }

    @Override
    public Student getStudent(String id) {
        // 业务规则 2：学生必须存在
        Student student = studentDao.findById(id);
        if (student == null) {
            throw new StudentNotFoundException("学号 " + id + " 不存在");
        }
        return student;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.findAll();
    }

    @Override
    public void updateStudent(Student student) {
        // 业务规则 3：要改的学生必须存在
        if (!studentDao.existsById(student.getId())) {
            throw new StudentNotFoundException("学号 " + student.getId() + " 不存在，无法修改");
        }
        studentDao.update(student);
    }

    @Override
    public void deleteStudent(String id) {
        // 业务规则 4：要删的学生必须存在
        if (!studentDao.existsById(id)) {
            throw new StudentNotFoundException("学号 " + id + " 不存在，无法删除");
        }
        studentDao.delete(id);
    }
}