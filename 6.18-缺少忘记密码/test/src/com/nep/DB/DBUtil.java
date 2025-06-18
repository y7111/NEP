package com.nep.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    // 数据库连接
    private static final String URL = "jdbc:mysql://localhost:3306/nep_db?useSSL=false&serverTimezone=UTC";
    // 数据库用户名
    private static final String USER = "root";
    // 数据库密码
    private static final String PASSWORD = "123456";

    static {
        try {
            // 注册MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     * @return Connection 对象，如果连接失败则返回 null
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库资源
     * @param rs ResultSet 对象
     * @param pstmt PreparedStatement 对象
     * @param conn Connection 对象
     */
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库资源失败！");
            e.printStackTrace();
        }
    }
}