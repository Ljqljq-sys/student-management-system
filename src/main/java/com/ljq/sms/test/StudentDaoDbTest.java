package com.ljq.sms.test;

import com.ljq.sms.dao.StudentDao;
import com.ljq.sms.dao.StudentDaoDbImpl;
import com.ljq.sms.entity.Student;

import java.util.List;

/**
 * 测试数据库版 DAO
 */
public class StudentDaoDbTest {

    public static void main(String[] args) {
        StudentDao dao = new StudentDaoDbImpl();

        System.out.println("=== 1. 查询全部 ===");
        List<Student> all = dao.findAll();
        System.out.println("共 " + all.size() + " 条");
        for (Student s : all) {
            System.out.println("  " + s);
        }

        System.out.println("\n=== 2. 按学号查询 ===");
        Student one = dao.findById("2024001");
        System.out.println("查到：" + one);

        System.out.println("\n=== 3. 判断是否存在 ===");
        System.out.println("2024001 存在吗？" + dao.existsById("2024001"));
        System.out.println("9999999 存在吗？" + dao.existsById("9999999"));

        System.out.println("\n=== 4. 添加 ===");
        dao.add(new Student("2024099", "测试新增", 20, "测试专业", 77.5));
        System.out.println("添加后共 " + dao.findAll().size() + " 条");

        System.out.println("\n=== 5. 修改 ===");
        Student s = dao.findById("2024099");
        s.setScore(99.0);
        s.setName("测试修改后");
        dao.update(s);
        System.out.println("修改后：" + dao.findById("2024099"));

        System.out.println("\n=== 6. 删除 ===");
        dao.delete("2024099");
        System.out.println("删除后共 " + dao.findAll().size() + " 条");
        System.out.println("2024099 还存在吗？" + dao.existsById("2024099"));
    }
}