package com.nep.service.impl;

import com.nep.entity.Admin;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.AdminService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminServiceImpl implements AdminService {
    @Override
    public boolean login(String loginCode, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isLogin = false;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT login_code FROM admin WHERE login_code = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                isLogin = true;
                System.out.println("管理员 " + loginCode + " 登录成功！");
            } else {
                System.out.println("管理员 " + loginCode + " 登录失败：账号或密码错误。");
            }
        } catch (SQLException e) {
            System.err.println("管理员登录失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return isLogin;
    }

    public boolean register(Admin admin) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String checkSql = "SELECT COUNT(*) FROM admin WHERE login_code = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, admin.getLoginCode());
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("注册失败：账号 " + admin.getLoginCode() + " 已存在。");
                return false;
            }
            DBUtil.close(rs, pstmt, null);

            String insertSql = "INSERT INTO admin (login_code, password, real_name) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, admin.getLoginCode());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getRealName());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("管理员 " + admin.getLoginCode() + " 注册成功！");
                return true;
            } else {
                System.out.println("管理员 " + admin.getLoginCode() + " 注册失败：未能插入数据。");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("管理员注册失败：数据库操作异常。");
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}