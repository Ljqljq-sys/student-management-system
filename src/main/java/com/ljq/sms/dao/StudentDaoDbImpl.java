package com.ljq.sms.dao;

import com.ljq.sms.entity.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生数据访问 —— 数据库实现
 * 用 MySQL 存储数据，替代文件存储
 */
public class StudentDaoDbImpl implements StudentDao {

    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/sms"
            + "?useSSL=false"
            + "&serverTimezone=Asia/Shanghai"
            + "&characterEncoding=utf8"
            + "&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "ljq070205";     // ⚠️ 改成你的密码

    /**
     * 获取数据库连接
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ==================== 增 ====================

    @Override
    public void add(Student student) {
        String sql = "INSERT INTO student (id, name, age, major, score) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getMajor());
            ps.setDouble(5, student.getScore());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("添加学生失败：" + e.getMessage(), e);
        }
    }

    // ==================== 查（单个） ====================

    @Override
    public Student findById(String id) {
        String sql = "SELECT * FROM student WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询学生失败：" + e.getMessage(), e);
        }

        return null;    // 查不到返回 null（和文件版行为一致）
    }

    // ==================== 查（全部） ====================

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM student";
        List<Student> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询全部学生失败：" + e.getMessage(), e);
        }

        return list;
    }

    // ==================== 改 ====================

    @Override
    public void update(Student student) {
        String sql = "UPDATE student SET name = ?, age = ?, major = ?, score = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getMajor());
            ps.setDouble(4, student.getScore());
            ps.setString(5, student.getId());       // ⚠️ WHERE 的 id 放最后

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("修改学生失败：" + e.getMessage(), e);
        }
    }

    // ==================== 删 ====================

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除学生失败：" + e.getMessage(), e);
        }
    }

    // ==================== 判断存在 ====================

    @Override
    public boolean existsById(String id) {
        String sql = "SELECT COUNT(*) FROM student WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询学生是否存在失败：" + e.getMessage(), e);
        }

        return false;
    }

    // ==================== 工具方法：ResultSet → Student ====================

    /**
     * 把结果集当前行转换成一个 Student 对象
     * 抽成方法，避免重复代码
     */
    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("major"),
                rs.getDouble("score")
        );
    }
}