package com.nep.test;

import com.nep.entity.ProvinceCity;
import com.nep.DB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        ProvinceCity p1 = new ProvinceCity();
        p1.setProvinceId(1001);
        p1.setProvinceName("辽宁省");
        List<String> city1 = new ArrayList<>();
        city1.add("沈阳市");
        city1.add("大连市");
        city1.add("鞍山市");
        city1.add("抚顺市");
        p1.setCityName(city1);

        ProvinceCity p2 = new ProvinceCity();
        p2.setProvinceId(1002);
        p2.setProvinceName("吉林省");
        List<String> city2 = new ArrayList<>();
        city2.add("长春市");
        city2.add("吉林市");
        city2.add("四平市");
        city2.add("公主岭市");
        p2.setCityName(city2);

        ProvinceCity p3 = new ProvinceCity();
        p3.setProvinceId(1003);
        p3.setProvinceName("黑龙江省");
        List<String> city3 = new ArrayList<>();
        city3.add("哈尔滨市");
        city3.add("大庆市");
        city3.add("齐齐哈尔市");
        p3.setCityName(city3);

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO province_city (province_id, province_name, city_names) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            ProvinceCity[] provinceCities = {p1, p2, p3};
            for (ProvinceCity pc : provinceCities) {
                pstmt.setInt(1, pc.getProvinceId());
                pstmt.setString(2, pc.getProvinceName());
                pstmt.setString(3, pc.getCityNamesAsString());
                int rows = pstmt.executeUpdate();
                System.out.println("插入省市数据 " + pc.getProvinceName() + " (ID: " + pc.getProvinceId() + ") 成功：" + (rows > 0));
                pstmt.clearParameters();
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.err.println("错误：省份ID已存在，跳过插入。");
            } else {
                System.err.println("插入省市数据失败：数据库操作异常。");
                e.printStackTrace();
            }
        } finally {
            DBUtil.close(null, pstmt, conn);
        }
    }
}