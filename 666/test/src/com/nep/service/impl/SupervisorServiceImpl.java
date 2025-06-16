package com.nep.service.impl;

import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.SupervisorService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupervisorServiceImpl implements SupervisorService {

    @Override
    public Supervisor login(String loginCode, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Supervisor supervisor = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT login_code, password, real_name, sex FROM supervisor WHERE login_code = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                supervisor = new Supervisor();
                supervisor.setLoginCode(rs.getString("login_code"));
                supervisor.setPassword(rs.getString("password"));
                supervisor.setRealName(rs.getString("real_name"));
                supervisor.setSex(rs.getString("sex"));

                NepsSelectAqiViewController.supervisor = supervisor;
                System.out.println("Supervisor " + loginCode + " 登录成功！");
            } else {
                System.out.println("Supervisor " + loginCode + " 登录失败：账号或密码错误。");
            }
        } catch (SQLException e) {
            System.err.println("Supervisor 登录失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return supervisor;
    }

    @Override
    public boolean register(Supervisor supervisor) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String checkSql = "SELECT COUNT(*) FROM supervisor WHERE login_code = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, supervisor.getLoginCode());
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("注册失败：账号 " + supervisor.getLoginCode() + " 已存在。");
                return false;
            }
            DBUtil.close(rs, pstmt, null);

            String insertSql = "INSERT INTO supervisor (login_code, password, real_name, sex) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, supervisor.getLoginCode());
            pstmt.setString(2, supervisor.getPassword());
            pstmt.setString(3, supervisor.getRealName());
            pstmt.setString(4, supervisor.getSex());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("用户 " + supervisor.getLoginCode() + " 注册成功！");
                return true;
            } else {
                System.out.println("用户 " + supervisor.getLoginCode() + " 注册失败：未能插入数据。");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Supervisor 注册失败：数据库操作异常。");
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}