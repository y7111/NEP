// test/src/com/nep/util/DBUtil.java
package com.nep.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    // 数据库连接URL，注意替换为你的MySQL服务器地址、端口、数据库名
    private static final String URL = "jdbc:mysql://localhost:3306/nep_db?useSSL=false&serverTimezone=UTC";
    // 数据库用户名
    private static final String USER = "root"; // 替换为你的MySQL用户名
    // 数据库密码
    private static final String PASSWORD = "123456"; // 替换为你的MySQL密码

    static {
        try {
            // 注册MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found.");
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
            // System.out.println("数据库连接成功！"); // 调试用，可以根据需要注释掉
        } catch (SQLException e) {
            System.err.println("数据库连接失败！请检查URL、用户名、密码或MySQL服务是否运行。");
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

    // 主方法用于测试数据库连接，非必需
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                System.out.println("DBUtil connection test successful!");
            }
        } finally {
            close(null, null, conn);
        }
    }
}