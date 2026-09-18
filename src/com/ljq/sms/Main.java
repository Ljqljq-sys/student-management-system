package com.ljq.sms;

import com.ljq.sms.entity.Student;
import com.ljq.sms.exception.DuplicateIdException;
import com.ljq.sms.exception.StudentNotFoundException;
import com.ljq.sms.service.StudentService;
import com.ljq.sms.service.StudentServiceImpl;

public class Main {
    public static void main(String[] args) {
        StudentService service = new StudentServiceImpl();

        System.out.println("=== 1. 添加学生 ===");
        try {
            service.addStudent(new Student("2024001", "ljq", 20, "计算机", 85.5));
            System.out.println("添加成功：2024001");
        } catch (DuplicateIdException e) {
            System.out.println("添加失败：" + e.getMessage());
        }

        // 再添加一次同样的学号 → 应该被拦住
        System.out.println("\n=== 2. 重复添加同一个学号 ===");
        try {
            service.addStudent(new Student("2024001", "张三", 21, "软件", 90.0));
            System.out.println("添加成功（不应该看到这行！）");
        } catch (DuplicateIdException e) {
            System.out.println("添加失败：" + e.getMessage());   // ← 应该看到这行
        }

        System.out.println("\n=== 3. 查询不存在的学生 ===");
        try {
            Student s = service.getStudent("9999999");
            System.out.println("查到了：" + s);
        } catch (StudentNotFoundException e) {
            System.out.println("查询失败：" + e.getMessage());   // ← 应该看到这行
        }

        System.out.println("\n=== 4. 删除不存在的学生 ===");
        try {
            service.deleteStudent("9999999");
            System.out.println("删除成功（不应该看到这行！）");
        } catch (StudentNotFoundException e) {
            System.out.println("删除失败：" + e.getMessage());   // ← 应该看到这行
        }

        System.out.println("\n=== 5. 当前所有学生 ===");
        for (Student s : service.getAllStudents()) {
            System.out.println(s);
        }
    }
}