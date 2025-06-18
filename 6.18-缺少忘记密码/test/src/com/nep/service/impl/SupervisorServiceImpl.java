package com.nep.service.impl;

import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.DB.DBUtil;
import com.nep.service.SupervisorService;
import com.nep.util.SHA512Util; // 导入SHA512Util

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
            String sql = "SELECT login_code, password, real_name, sex FROM supervisor WHERE login_code = ? AND password = ?"; // SQL查询，直接比较加密后的密码
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);
            // 对输入的密码进行加密，然后与数据库中的密文比较
            // 假设phoneNumber或realName可以用作盐值，这里使用loginCode作为盐值
            String encryptedPassword = SHA512Util.encrypt(password, loginCode); // 使用loginCode作为盐值进行加密
            pstmt.setString(2, encryptedPassword); // 使用加密后的密码进行比对

            rs = pstmt.executeQuery();

            if (rs.next()) {
                supervisor = new Supervisor();
                supervisor.setLoginCode(rs.getString("login_code"));
                supervisor.setPassword(rs.getString("password")); // 这里获取的是加密后的密码
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
        } catch (IllegalArgumentException e) {
            System.err.println("登录失败：加密参数无效。" + e.getMessage());
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
            DBUtil.close(rs, pstmt, null); // 关闭用于查询的ResultSet和PreparedStatement，但保持Connection开启

            String insertSql = "INSERT INTO supervisor (login_code, password, real_name, sex) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, supervisor.getLoginCode());
            // 注册时加密密码并存储密文
            // 同样使用loginCode作为盐值，如果手机号在Supervisor实体中可用，用手机号会更好
            String encryptedPassword = SHA512Util.encrypt(supervisor.getPassword(), supervisor.getLoginCode()); // 使用loginCode作为盐值进行加密
            pstmt.setString(2, encryptedPassword); // 存储加密后的密码
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
        } catch (IllegalArgumentException e) {
            System.err.println("注册失败：加密参数无效。" + e.getMessage());
            return false;
        } finally {
            DBUtil.close(null, pstmt, conn); // 关闭所有资源
        }
    }
}