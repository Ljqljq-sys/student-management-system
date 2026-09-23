package com.ljq.sms.ui;

import com.ljq.sms.entity.Student;
import com.ljq.sms.exception.DuplicateIdException;
import com.ljq.sms.exception.StudentNotFoundException;
import com.ljq.sms.service.StudentService;
import com.ljq.sms.service.StudentServiceImpl;

import java.util.List;
import java.util.Scanner;

/**
 * 学生管理系统 - 用户界面
 * 负责：显示菜单、读取输入、调用 Service、展示结果
 */
public class StudentUI {

    // UI 只依赖 Service，不直接碰 Dao
    private final StudentService studentService = new StudentServiceImpl();

    // Scanner 用来读用户输入，整个类共用这一个
    private final Scanner scanner = new Scanner(System.in);

    /**
     * 启动程序：显示菜单，循环处理用户操作
     */
    public void start() {
        System.out.println("欢迎使用学生管理系统！");

        while (true) {
            printMenu();
            int choice = readInt("请输入选项：");

            switch (choice) {
                case 1:
                    // addStudent();
                    addStudent();
                    break;
                case 2:
                    // findStudent();
                    findStudent();
                    break;
                case 3:
                    // showAllStudents();
                    showAllStudents();
                    break;
                case 4:
                    // updateStudent();
                    updateStudent();
                    break;
                case 5:
                    // deleteStudent();
                    deleteStudent();
                    break;
                case 6:
                    searchByName();
                    break;
                case 7:
                    sortByScore();
                    break;
                case 8:
                    showStatistics();
                    break;
                case 0:
                    System.out.println("再见！");
                    return;                 // ⚠️ 用 return 才能真正退出整个方法
                default:
                    System.out.println("输入有误，请输入 0~8 之间的数字");
            }
        }
    }

    /**
     * 打印菜单
     */
    private void printMenu() {
        System.out.println();
        System.out.println("========== 学生管理系统 ==========");
        System.out.println("  1. 添加学生");
        System.out.println("  2. 查询学生");
        System.out.println("  3. 显示全部学生");
        System.out.println("  4. 修改学生信息");
        System.out.println("  5. 删除学生");
        System.out.println("  6. 按姓名搜索");
        System.out.println("  7. 按成绩排序");
        System.out.println("  8. 成绩统计");
        System.out.println("  0. 退出系统");
        System.out.println("==================================");
    }

    /**
     * 读取一个整数输入，输入不合法会一直要求重输
     */
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("输入不能为空，请重新输入");
                continue;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("请输入数字！");
            }
        }
    }
    /**
     * 显示全部学生
     */
    private void showAllStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("暂无学生数据");
            return;
        }

        System.out.println("\n共 " + students.size() + " 名学生：");
        System.out.println("----------------------------------------");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("----------------------------------------");
    }

    /**
     * 添加学生
     */
    private void addStudent() {
        System.out.println("\n--- 添加学生 ---");

        String id = readNonEmpty("请输入学号：");
        String name = readNonEmpty("请输入姓名：");
        int age = readInt("请输入年龄：");
        String major = readNonEmpty("请输入专业：");
        double score = readDouble("请输入成绩：");

        Student student = new Student(id, name, age, major, score);

        try {
            studentService.addStudent(student);
            System.out.println("✅ 添加成功！");
        } catch (DuplicateIdException e) {
            System.out.println("❌ 添加失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {              // ← 加这个
            System.out.println("❌ 添加失败：" + e.getMessage());
        }

    }

    /**
     * 读取一个小数输入
     */
    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("输入不能为空，请重新输入");
                continue;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("请输入数字！");
            }
        }
    }

    /**
     * 找学生
     */
    private void findStudent() {
        String id = readNonEmpty("\n请输入要查询的学号：");

        try {
            Student s = studentService.getStudent(id);
            System.out.println("查询结果：" + s);
        } catch (StudentNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    /**
     * 修改学生信息
     */
    private void updateStudent() {
        System.out.println("\n--- 修改学生信息 ---");

        // 1. 读学号
        String id = readNonEmpty("请输入要修改的学号：");

        // 2. 先查出原学生（查不到会抛异常，被下面的 catch 接住）
        Student old;
        try {
            old = studentService.getStudent(id);
        } catch (StudentNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
            return;                      // ← 查不到就直接结束这个方法
        }

        // 3. 显示原信息
        System.out.println("当前信息：" + old);

        // 4. 读新值
        System.out.print("新姓名（回车保持不变）：");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            old.setName(name);
        }

        System.out.print("新年龄（回车保持不变）：");
        String ageStr = scanner.nextLine().trim();
        if (!ageStr.isEmpty()) {
            try {
                old.setAge(Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                System.out.println("年龄格式不对，保持不变");
            }
        }

        System.out.print("新专业（回车保持不变）：");
        String major = scanner.nextLine().trim();
        if (!major.isEmpty()) {
            old.setMajor(major);
        }

        System.out.print("新成绩（回车保持不变）：");
        String scoreStr = scanner.nextLine().trim();
        if (!scoreStr.isEmpty()) {
            try {
                old.setScore(Double.parseDouble(scoreStr));
            } catch (NumberFormatException e) {
                System.out.println("成绩格式不对，保持不变");
            }
        }

        // 5. 提交修改
        try {
            studentService.updateStudent(old);
            System.out.println("✅ 修改成功！");
        } catch (StudentNotFoundException e) {
            System.out.println("❌ 修改失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {              // ← 加这个
            System.out.println("❌ 修改失败：" + e.getMessage());
        }
    }


    /**
     * 删除学生
     */
    private void deleteStudent() {
        System.out.println("\n--- 删除学生 ---");

        String id = readNonEmpty("请输入要删除的学号：");

        // 先查出来给用户确认
        try {
            Student s = studentService.getStudent(id);
            System.out.println("即将删除：" + s);
            System.out.print("确认删除吗？(y/n)：");
            String confirm = scanner.nextLine().trim();
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("已取消");
                return;
            }
        } catch (StudentNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
            return;
        }

        // 真正删除
        try {
            studentService.deleteStudent(id);
            System.out.println("✅ 删除成功！");
        } catch (StudentNotFoundException e) {
            System.out.println("❌ 删除失败：" + e.getMessage());
        }
    }

    /**
     * 读取一个非空字符串
     */
    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("输入不能为空，请重新输入");
        }
    }

    /**
     * 按姓名模糊搜索
     */
    private void searchByName() {
        System.out.println("\n--- 按姓名搜索 ---");
        String keyword = readNonEmpty("请输入姓名关键字：");

        List<Student> result = studentService.searchByName(keyword);

        if (result.isEmpty()) {
            System.out.println("没有找到姓名包含 \"" + keyword + "\" 的学生");
            return;
        }

        System.out.println("\n找到 " + result.size() + " 名学生：");
        System.out.println("----------------------------------------");
        for (Student s : result) {
            System.out.println(s);
        }
        System.out.println("----------------------------------------");
    }

    /**
     * 按成绩排序显示
     */
    private void sortByScore() {
        System.out.println("\n--- 按成绩排序 ---");
        System.out.println("  1. 从低到高");
        System.out.println("  2. 从高到低");
        int choice = readInt("请选择：");

        boolean ascending = (choice == 1);
        List<Student> list = studentService.sortByScore(ascending);

        if (list.isEmpty()) {
            System.out.println("暂无学生数据");
            return;
        }

        System.out.println("\n" + (ascending ? "成绩从低到高：" : "成绩从高到低："));
        System.out.println("----------------------------------------");
        for (Student s : list) {
            System.out.println(s);
        }
        System.out.println("----------------------------------------");
    }

    /**
     * 成绩统计
     */
    private void showStatistics() {
        System.out.println("\n--- 成绩统计 ---");
        System.out.println(studentService.getStatistics());
    }
}