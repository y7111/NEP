package com.nep.service.impl;

import com.nep.controller.NepsSelectAqiViewController;
import com.nep.entity.Supervisor;
import com.nep.DB.DBUtil;
import com.nep.service.SupervisorService;
import com.nep.util.SHA512Util;

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
            // SQL查询需要包含 security_question 和 security_answer 字段，即使登录时不需要，为了获取完整的Supervisor对象
            String sql = "SELECT login_code, password, real_name, sex, security_question, security_answer FROM supervisor WHERE login_code = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);
            String encryptedPassword = SHA512Util.encrypt(password, loginCode);
            pstmt.setString(2, encryptedPassword);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                supervisor = new Supervisor();
                supervisor.setLoginCode(rs.getString("login_code"));
                supervisor.setPassword(rs.getString("password"));
                supervisor.setRealName(rs.getString("real_name"));
                supervisor.setSex(rs.getString("sex"));
                supervisor.setSecurityQuestion(rs.getString("security_question")); // 获取密保问题
                supervisor.setSecurityAnswer(rs.getString("security_answer"));   // 获取密保答案

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
                // 如果账号存在，则尝试更新其信息，特别是在注册流程中，如果用户尝试再次注册，可能需要更新其信息
                // 但通常注册是针对新用户，这里保持为false，要求用户如果已注册则走忘记密码流程
                return false;
            }
            DBUtil.close(rs, pstmt, null);

            // 插入操作需要包含 security_question 和 security_answer 字段
            String insertSql = "INSERT INTO supervisor (login_code, password, real_name, sex, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, supervisor.getLoginCode());
            String encryptedPassword = SHA512Util.encrypt(supervisor.getPassword(), supervisor.getLoginCode());
            pstmt.setString(2, encryptedPassword);
            pstmt.setString(3, supervisor.getRealName());
            pstmt.setString(4, supervisor.getSex());
            pstmt.setString(5, supervisor.getSecurityQuestion()); // 插入密保问题
            pstmt.setString(6, supervisor.getSecurityAnswer());   // 插入密保答案

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
            DBUtil.close(null, pstmt, conn);
        }
    }

    /**
     * 新增方法：根据登录码获取 Supervisor 对象，包含其密保信息。
     */
    @Override
    public Supervisor getSupervisorByLoginCode(String loginCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Supervisor supervisor = null;

        try {
            conn = DBUtil.getConnection();
            // 查询所有相关字段，包括密保问题和答案
            String sql = "SELECT login_code, password, real_name, sex, security_question, security_answer FROM supervisor WHERE login_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                supervisor = new Supervisor();
                supervisor.setLoginCode(rs.getString("login_code"));
                supervisor.setPassword(rs.getString("password"));
                supervisor.setRealName(rs.getString("real_name"));
                supervisor.setSex(rs.getString("sex"));
                supervisor.setSecurityQuestion(rs.getString("security_question"));
                supervisor.setSecurityAnswer(rs.getString("security_answer"));
                System.out.println("成功找到 Supervisor: " + loginCode);
            } else {
                System.out.println("未找到 Supervisor: " + loginCode);
            }
        } catch (SQLException e) {
            System.err.println("根据 loginCode 获取 Supervisor 失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return supervisor;
    }

    /**
     * 新增方法：更新 Supervisor 信息（特别是密码和密保信息）。
     * 这个方法将在忘记密码流程中用于更新用户的密码。
     */
    @Override
    public boolean updateSupervisor(Supervisor supervisor) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            // 更新操作，通过 login_code 定位用户，更新密码、真实姓名、性别、密保问题和答案
            // 注意: 这里应该更新所有可能改变的字段，如果只有密码改变，可以只更新密码字段
            String sql = "UPDATE supervisor SET password = ?, real_name = ?, sex = ?, security_question = ?, security_answer = ? WHERE login_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, supervisor.getPassword()); // 密码在这里应该是已经加密过的
            pstmt.setString(2, supervisor.getRealName());
            pstmt.setString(3, supervisor.getSex());
            pstmt.setString(4, supervisor.getSecurityQuestion());
            pstmt.setString(5, supervisor.getSecurityAnswer());
            pstmt.setString(6, supervisor.getLoginCode()); // WHERE 条件

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Supervisor " + supervisor.getLoginCode() + " 信息更新成功！");
                return true;
            } else {
                System.out.println("Supervisor " + supervisor.getLoginCode() + " 信息更新失败：用户不存在或数据未改变。");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Supervisor 信息更新失败：数据库操作异常。");
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }


}