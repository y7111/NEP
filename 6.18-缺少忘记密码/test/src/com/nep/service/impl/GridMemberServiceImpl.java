package com.nep.service.impl;

import com.nep.entity.GridMember;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.GridMemberService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GridMemberServiceImpl implements GridMemberService {
    @Override
    public GridMember login(String loginCode, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        GridMember gridMember = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT login_code, password, real_name, gm_tel, state FROM grid_member WHERE login_code = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginCode);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                gridMember = new GridMember();
                gridMember.setLoginCode(rs.getString("login_code"));
                gridMember.setPassword(rs.getString("password"));
                gridMember.setRealName(rs.getString("real_name"));
                gridMember.setGmTel(rs.getString("gm_tel"));
                gridMember.setState(rs.getString("state"));
                System.out.println("网格员 " + loginCode + " 登录成功！");
            } else {
                System.out.println("网格员 " + loginCode + " 登录失败：账号或密码错误。");
            }
        } catch (SQLException e) {
            System.err.println("网格员登录失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return gridMember;
    }

    public boolean register(GridMember gm) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String checkSql = "SELECT COUNT(*) FROM grid_member WHERE login_code = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, gm.getLoginCode());
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("注册失败：账号 " + gm.getLoginCode() + " 已存在。");
                return false;
            }
            DBUtil.close(rs, pstmt, null);

            String insertSql = "INSERT INTO grid_member (login_code, password, real_name, gm_tel, state) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setString(1, gm.getLoginCode());
            pstmt.setString(2, gm.getPassword());
            pstmt.setString(3, gm.getRealName());
            pstmt.setString(4, gm.getGmTel());
            pstmt.setString(5, gm.getState());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("网格员 " + gm.getLoginCode() + " 注册成功！");
                return true;
            } else {
                System.out.println("网格员 " + gm.getLoginCode() + " 注册失败：未能插入数据。");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("网格员注册失败：数据库操作异常。");
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}