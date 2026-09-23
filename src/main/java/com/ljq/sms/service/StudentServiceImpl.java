package com.ljq.sms.service;

import com.ljq.sms.dao.StudentDao;
import com.ljq.sms.dao.StudentDaoDbImpl;
import com.ljq.sms.entity.Student;
import com.ljq.sms.exception.DuplicateIdException;
import com.ljq.sms.exception.StudentNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.List;

/**
 * 学生业务逻辑实现
 * 负责业务规则：学号不能重复、学生必须存在
 */
public class StudentServiceImpl implements StudentService {

    // Service 持有 Dao，通过它访问数据（这叫"组合"）
    private final StudentDao studentDao = new StudentDaoDbImpl();
    //组合**（Composition）—— 一个对象"拥有"另一个对象。
    //StudentServiceImpl 有一个 StudentDao

    @Override
    public void addStudent(Student student) {
        // 业务规则 1：学号不能重复
        //DAO 的 add 是"无脑存"；Service 的 addStudent 是"先检查，再存"。
        if (studentDao.existsById(student.getId())) {
            throw new DuplicateIdException("学号 " + student.getId() + " 已存在");
        } if (student.getScore() < 0 || student.getScore() > 100) {
            throw new IllegalArgumentException("成绩必须在 0~100 之间");
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
        } if (student.getScore() < 0 || student.getScore() > 100) {
            throw new IllegalArgumentException("成绩必须在 0~100 之间");
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

    @Override
    public List<Student> searchByName(String keyword) {
        List<Student> result = new ArrayList<>();
        for (Student s : studentDao.findAll()) {
            if (s.getName().contains(keyword)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Student> sortByScore(boolean ascending) {
        List<Student> list = studentDao.findAll();
        if (ascending) {
            list.sort(Comparator.comparingDouble(Student::getScore));
        } else {
            list.sort(Comparator.comparingDouble(Student::getScore).reversed());
        }
        return list;
    }

    @Override
    public String getStatistics() {
        List<Student> list = studentDao.findAll();
        if (list.isEmpty()) {
            return "暂无数据";
        }

        double sum = 0;
        double max = list.get(0).getScore();
        double min = list.get(0).getScore();
        int passCount = 0;

        for (Student s : list) {
            double score = s.getScore();
            sum += score;
            if (score > max) max = score;
            if (score < min) min = score;
            if (score >= 60) passCount++;
        }

        double avg = sum / list.size();
        double passRate = passCount * 100.0 / list.size();

        return String.format(
                "总人数：%d%n平均分：%.2f%n最高分：%.1f%n最低分：%.1f%n及格人数：%d%n及格率：%.1f%%",
                list.size(), avg, max, min, passCount, passRate);
    }
}