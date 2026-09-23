package com.ljq.sms.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC 连接测试
 * 目标：验证 Java 能连上 MySQL，并能查出数据
 */
public class JdbcTest {

    public static void main(String[] args) {
        // 1. 连接地址
        //    jdbc:mysql://主机:端口/数据库名?参数
        String url = "jdbc:mysql://localhost:3306/sms"
                + "?useSSL=false"
                + "&serverTimezone=Asia/Shanghai"
                + "&characterEncoding=utf8";

        String user = "root";
        String password = "ljq070205";     // ⚠️ 改成你自己的密码

        System.out.println("正在连接数据库...");

        // try-with-resources：括号里的资源会自动关闭
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM student")) {

            System.out.println("✅ 连接成功！");
            System.out.println("----------------------------------------");

            // 遍历结果集
            int count = 0;
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String major = rs.getString("major");
                double score = rs.getDouble("score");

                System.out.printf("%s | %s | %d | %s | %.1f%n",
                        id, name, age, major, score);
                count++;
            }

            System.out.println("----------------------------------------");
            System.out.println("共 " + count + " 条记录");

        } catch (SQLException e) {
            System.out.println("❌ 连接失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}