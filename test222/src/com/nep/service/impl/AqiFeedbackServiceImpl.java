package com.nep.service.impl;

import com.nep.entity.AqiFeedback;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.AqiFeedbackService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // 用于获取自增ID

public class AqiFeedbackServiceImpl implements AqiFeedbackService {
    @Override
    public void saveFeedBack(AqiFeedback afb) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO aqifeedback (af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, afb.getAfName());
            pstmt.setString(2, afb.getProviceName());
            pstmt.setString(3, afb.getCityName());
            pstmt.setString(4, afb.getAddress());
            pstmt.setString(5, afb.getInformation());
            pstmt.setString(6, afb.getEstimateGrade());
            pstmt.setString(7, afb.getDateFeedback()); // 使用新的 getter
            pstmt.setString(8, afb.getState());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    afb.setAfId(rs.getInt(1));
                }
                System.out.println("反馈信息提交成功，编号：" + afb.getAfId());
            } else {
                System.out.println("反馈信息提交失败。");
            }
        } catch (SQLException e) {
            System.err.println("保存反馈信息失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    @Override
    public void assignGridMember(String afId, String realName) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE aqifeedback SET gm_name = ?, state = '已指派' WHERE af_id = ? AND state = '未指派'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, realName);
            pstmt.setInt(2, Integer.parseInt(afId));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("反馈信息编号 " + afId + " 指派网格员 " + realName + " 成功！");
            } else {
                System.out.println("反馈信息编号 " + afId + " 指派失败，可能已指派或不存在。");
            }
        } catch (SQLException e) {
            System.err.println("指派网格员失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }

    @Override
    public void confirmData(AqiFeedback afb) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE aqifeedback SET state = '已实测', confirm_date = ?, co = ?, so2 = ?, pm = ?, confirm_level = ?, confirm_explain = ? WHERE af_id = ? AND gm_name = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, afb.getConfirmDate());
            pstmt.setDouble(2, afb.getCo());
            pstmt.setDouble(3, afb.getSo2());
            pstmt.setDouble(4, afb.getPm());
            pstmt.setString(5, afb.getConfirmLevel());
            pstmt.setString(6, afb.getConfirmExplain());
            pstmt.setInt(7, afb.getAfId());
            pstmt.setString(8, afb.getGmName());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("反馈信息编号 " + afb.getAfId() + " 实测数据提交成功！");
            } else {
                System.out.println("反馈信息编号 " + afb.getAfId() + " 实测数据提交失败，可能未指派给该网格员或不存在。");
            }
        } catch (SQLException e) {
            System.err.println("提交实测AQI数据失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}