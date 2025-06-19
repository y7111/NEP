package com.nep.test;

import com.nep.entity.GridMember;
import com.nep.DB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestGM {
    public static void main(String[] args) {
        GridMember gm1 = new GridMember();
        gm1.setLoginCode("2024001");
        gm1.setPassword("111");
        gm1.setRealName("郭晓川");
        gm1.setGmTel("13888888888");
        gm1.setState("工作中");

        GridMember gm2 = new GridMember();
        gm2.setLoginCode("2024002");
        gm2.setPassword("222");
        gm2.setRealName("韩德君");
        gm2.setGmTel("13555555555");
        gm2.setState("工作中");

        GridMember gm3 = new GridMember();
        gm3.setLoginCode("2024003");
        gm3.setPassword("333");
        gm3.setRealName("李晓旭");
        gm3.setGmTel("13444444444");
        gm3.setState("工作中");

        GridMember gm4 = new GridMember();
        gm4.setLoginCode("2024004");
        gm4.setPassword("444");
        gm4.setRealName("赵继伟");
        gm4.setGmTel("13222222222");
        gm4.setState("休假中");

        GridMember gm5 = new GridMember();
        gm5.setLoginCode("2024005");
        gm5.setPassword("555");
        gm5.setRealName("易建联");
        gm5.setGmTel("13666666666");
        gm5.setState("工作中");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO grid_member (login_code, password, real_name, gm_tel, state) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            GridMember[] gridMembers = {gm1, gm2, gm3, gm4, gm5};
            for (GridMember gm : gridMembers) {
                pstmt.setString(1, gm.getLoginCode());
                pstmt.setString(2, gm.getPassword());
                pstmt.setString(3, gm.getRealName());
                pstmt.setString(4, gm.getGmTel());
                pstmt.setString(5, gm.getState());
                int rows = pstmt.executeUpdate();
                System.out.println("插入网格员 " + gm.getRealName() + " (loginCode: " + gm.getLoginCode() + ") 成功：" + (rows > 0));
                pstmt.clearParameters();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("错误：网格员账号已存在，跳过插入。");
            } else {
                System.err.println("插入网格员数据失败：数据库操作异常。");
                e.printStackTrace();
            }
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}