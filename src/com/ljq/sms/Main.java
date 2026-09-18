package com.ljq.sms;

import com.ljq.sms.dao.StudentDao;
import com.ljq.sms.dao.StudentDaoFileImpl;
import com.ljq.sms.entity.Student;

public class Main {
    public static void main(String[] args) {
        StudentDao dao = new StudentDaoFileImpl();

        // ========== 1. 添加 3 个学生 ==========
        dao.add(new Student("2024001", "ljq", 20, "计算机", 85.5));
        dao.add(new Student("2024002", "李四", 21, "软件工程", 92.0));
        dao.add(new Student("2024003", "王五", 19, "网络工程", 58.0));

        // ========== 2. 显示全部 ==========
        System.out.println("=== 全部学生 ===");
        for (Student s : dao.findAll()) {
            System.out.println(s);
        }

        // ========== 3. 按学号查 ==========
        System.out.println("\n=== 查 2024002 ===");
        System.out.println(dao.findById("2024002"));

        // ========== 4. 修改王五的成绩 ==========
        Student s = dao.findById("2024003");
        s.setScore(88.0);
        dao.update(s);

        // ========== 5. 删除 ljq ==========
        dao.delete("2024001");

        System.out.println("\n=== 修改 + 删除后 ===");
        for (Student stu : dao.findAll()) {
            //把 dao.findAll() 里的每一个元素，依次取出来放进 stu，然后执行循环体，: 读作"in"。
            System.out.println(stu);
        }
        //这个写法与传统写法是一样的效果，但是方便一点
        /*
            List<Student> list = dao.findAll();//创造一个list变量去接受findall返回的结果
            for(int i=0;i<list.size();i++){
            Student stu = list.get(i);
            System.out.println(stu);
        }//注意，不能用这个遍历使用删除方法
         */

        // ========== 6. 查不存在的学号 ==========
        System.out.println("\n=== 查 9999999（不存在）===");
        System.out.println(dao.findById("9999999"));

        // ========== 7. 判断存在 ==========
        System.out.println("\n=== 2024002 是否存在 ===");
        System.out.println(dao.existsById("2024002"));
        System.out.println("=== 9999999 是否存在 ===");
        System.out.println(dao.existsById("9999999"));

        // ========== 8. 统计一下 ==========
        System.out.println("\n=== 当前学生总数 ===");
        System.out.println(dao.findAll().size() + " 人");
    }
}