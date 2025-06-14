package com.nep.test;

import com.nep.entity.Aqi;
import com.nep.DB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test3 {
    public static void main(String[] args) {
        Aqi a1 = new Aqi("一级", "空气质量令人满意,基本无空气污染", "优");
        Aqi a2 = new Aqi("二级", "空气质量可接受,但某些污染物可能对极少数异常敏感人群健康有较弱的影响", "良");
        Aqi a3 = new Aqi("三级", "易感人群症状有轻度加剧,健康人群出现刺激症状", "轻度污染");
        Aqi a4 = new Aqi("四级", "进一步加剧易感人群症状,可能对健康人群心脏、呼吸系统有影响", "中度污染");
        Aqi a5 = new Aqi("五级", "心脏病和肺病患者症状显著加剧,运动耐受力降低,健康人群普遍出现症状", "重度污染");
        Aqi a6 = new Aqi("六级", "健康人群耐受力降低,有明显强烈症状,提前出现某些疾病", "严重污染");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO aqi (level, explain_desc, impact) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            Aqi[] aqiLevels = {a1, a2, a3, a4, a5, a6};
            for (Aqi aqi : aqiLevels) {
                pstmt.setString(1, aqi.getLevel());
                pstmt.setString(2, aqi.getExplain());
                pstmt.setString(3, aqi.getImpact());
                int rows = pstmt.executeUpdate();
                System.out.println("插入AQI等级 '" + aqi.getLevel() + "' 成功：" + (rows > 0));
                pstmt.clearParameters();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("错误：AQI等级已存在，跳过插入。");
            } else {
                System.err.println("插入AQI数据失败：数据库操作异常。");
                e.printStackTrace();
            }
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}