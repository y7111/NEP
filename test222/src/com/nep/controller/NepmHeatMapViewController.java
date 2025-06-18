package com.nep.controller;

import com.nep.DB.DBUtil;
import com.nep.dto.AqiLimitDto;
import com.nep.util.CommonUtil;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label; // 新增导入
import javafx.scene.text.Font; // 新增导入
import javafx.geometry.Bounds; // 新增导入

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NepmHeatMapViewController implements Initializable {

    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private Pane mapPane;

    //利用哈希map可以实现 用城市（键）去找值
    //去寻找对应的polygon 城市名称
    private Map<String, Polygon> cityPolygons = new HashMap<>();
    private Map<String, Label> cityLabels = new HashMap<>(); // 存储城市名称的Label

    //aqi对应的颜色 也利用键值对
    private Map<String, String> aqiLevelColors = new HashMap<>();
    //简化城市坐标
    private Map<String, double[]> simplifiedCityCoordinates = new HashMap<>();
    // cityToProvinceMap 已确认不再使用，可以删除其声明和初始化。


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 填充月份选择下拉框：从1月到12月
        for (int i = 1; i <= 12; i++) {
            monthComboBox.getItems().add(i + "月");
        }

        // 初始化AQI等级到颜色的映射。
        // 这些键（例如“一级”、“二级”）必须与数据库aqifeedback表中confirm_level字段存储的实际文本值完全一致。
        aqiLevelColors.put("一级", "#02E300");
        aqiLevelColors.put("二级", "#FFFF00");
        aqiLevelColors.put("三级", "#FF7E00");
        aqiLevelColors.put("四级", "#FE0000");
        aqiLevelColors.put("五级", "#98004B");
        aqiLevelColors.put("六级", "#7E0123");
        aqiLevelColors.put("无数据", "#CCCCCC");


        //城市坐标
        simplifiedCityCoordinates.put("沈阳市", new double[]{400, 200, 450, 210, 440, 250, 390, 240});
        simplifiedCityCoordinates.put("大连市", new double[]{480, 350, 520, 360, 510, 400, 470, 390});
        simplifiedCityCoordinates.put("鞍山市", new double[]{420, 260, 460, 270, 450, 300, 410, 290});
        simplifiedCityCoordinates.put("抚顺市", new double[]{430, 150, 470, 160, 460, 190, 420, 180});

        simplifiedCityCoordinates.put("长春市", new double[]{300, 100, 350, 110, 340, 150, 290, 140});
        simplifiedCityCoordinates.put("吉林市", new double[]{360, 80, 400, 90, 390, 130, 350, 120});
        simplifiedCityCoordinates.put("四平市", new double[]{250, 160, 290, 170, 280, 200, 240, 190});
        simplifiedCityCoordinates.put("公主岭市", new double[]{280, 180, 320, 190, 310, 220, 270, 210});

        simplifiedCityCoordinates.put("哈尔滨市", new double[]{200, 20, 250, 30, 240, 70, 190, 60});
        simplifiedCityCoordinates.put("大庆市", new double[]{150, 80, 190, 90, 180, 120, 140, 110});
        simplifiedCityCoordinates.put("齐齐哈尔市", new double[]{100, 10, 140, 20, 130, 60, 90, 50});

        // 绘制初始地图底图（所有城市的多边形和名称标签，默认灰色）
        drawBaseMap();

    }

    /**
     * 绘制地图底图（所有城市的多边形，并放置城市名称Label）。
     * 多边形默认填充为“无数据”颜色。
     * 。
     */
    public void drawBaseMap() {
        ///??? get budao 反正就是保证开始的时候又是空的pane
        mapPane.getChildren().clear();
        cityPolygons.clear();
        cityLabels.clear();


        // 遍历所有在simplifiedCityCoordinates中预定义的城市
        for (Map.Entry<String, double[]> entry : simplifiedCityCoordinates.entrySet()) {
            String cityName = entry.getKey();    // 获取城市名
            double[] points = entry.getValue();  // 获取该城市多边形的顶点坐标数组

            Polygon polygon = new Polygon(); //chuangjian polygon

            // 将double[]数组（基本数据类型数组）转换为List<Double>（Double对象列表），
            // 因为Polygon.getPoints().addAll()方法需要List<Double>类型的参数。
            List<Double> doublePoints = Arrays.stream(points).boxed().collect(Collectors.toList());
            polygon.getPoints().addAll(doublePoints);

            polygon.setFill(Color.web(aqiLevelColors.get("无数据"))); // 设置多边形默认填充颜色为灰色（“无数据”状态）
            polygon.setStroke(Color.DARKGRAY); // 设置多边形边框颜色
            polygon.setStrokeWidth(1); // 设置多边形边框粗细



            // 将创建好的多边形存储到cityPolygons Map中
            //方便使用polygon
            cityPolygons.put(cityName, polygon);
            // 将多边形添加到mapPane容器的子节点列表中，使其在界面上显示出来
            mapPane.getChildren().add(polygon);


            Label cityLabel = new Label(cityName);
            cityLabel.setFont(new Font("System Bold", 10)); // 设置Label的字体为粗体
            cityLabel.setTextFill(Color.BLACK);

            // 计算多边形的中心点，用于放置Label
            // getBoundsInLocal()

            Bounds bounds = polygon.getBoundsInLocal();
            double centerX = bounds.getMinX() + bounds.getWidth() / 2; // 计算边界框的X轴中心点
            double centerY = bounds.getMinY() + bounds.getHeight() / 2; // 计算边界框的Y轴中心点

            // 调整Label的布局位置，使其文本大致居中于多边形的中心点

            cityLabel.setLayoutX(centerX - 20);
            cityLabel.setLayoutY(centerY -  20 );

            // 将创建好的Label存储到cityLabels Map中，方便后续查找和更新
            cityLabels.put(cityName, cityLabel);
            // 将Label添加到mapPane容器的子节点列表中，使其在界面上显示出来
            mapPane.getChildren().add(cityLabel);


        }
    }

    /**
     * 显示热力图，根据用户选择的月份从数据库获取AQI数据并着色地图。
     */

    public void showHeatmap() {
        String selectedMonthText = monthComboBox.getValue(); // 获取用户选择的月份文本（例如“1月”）


        // 检查用户是否选择了有效的月份
        if (selectedMonthText == null || selectedMonthText.isEmpty() || selectedMonthText.equals("选择月份")) {
            JavafxUtil.showAlert(null, "选择月份", "请选择一个月份", "请从下拉框中选择要显示热力图的月份。", "warn");
            return; // 未选择月份，直接返回
        }

        // 将月份文本（例如“1月”）转换为整数（1），调用parseint
        int month = Integer.parseInt(selectedMonthText.replace("月", ""));
        System.out.println("HeatmapViewController: Showing heatmap for month: " + month); // 调试信息

        // 重新绘制底图。清除Pane中的所有旧图形，并重置所有多边形为默认的灰色
        drawBaseMap();

        // 从数据库获取指定月份每个城市的AQI数据
        Map<String, String> cityAqiLevels = getMonthlyAqiData(month);


        // 遍历从数据库获取的AQI数据，根据AQI等级为相应的城市多边形着色
        for (Map.Entry<String, String> entry : cityAqiLevels.entrySet()) {
            String cityName = entry.getKey();
            String aqiLevel = entry.getValue();

            // 根据城市名获取对应的多边形对象
            Polygon polygon = cityPolygons.get(cityName);
            // 根据城市名获取对应的Label对象
            Label cityLabel = cityLabels.get(cityName); // 使用cityName作为键

            if (polygon != null) { // 如果找到了对应的城市多边形
                // 从aqiLevelColors获取对应AQI等级的颜色代码
                String colorHex = aqiLevelColors.get(aqiLevel);

                if (colorHex != null) { // 如果找到了对应的颜色
                    polygon.setFill(Color.web(colorHex));
                    Tooltip.install(polygon, new Tooltip(cityName + "\nAQI: " + aqiLevel));

                    if (cityLabel != null) {
                        cityLabel.setTextFill(Color.WHITE);
                        cityLabel.setFont(new Font("System Bold", 11));
                    }
                } else {
                    if (cityLabel != null) {
                        cityLabel.setTextFill(Color.BLACK);
                        cityLabel.setFont(new Font("System", 10));
                    }
                }
            }
        }

        JavafxUtil.showAlert(null, "热力图更新", "污染热力图已更新", selectedMonthText + "的污染数据已加载并显示。", "info");
    }

    /**
     * 从数据库获取指定月份的城市AQI数据。
     * 返回一个Map，键是城市名（String），值是该城市的AQI等级文本（String）。
     * 此方法查询的是每月1号的数据。
     */
    private Map<String, String> getMonthlyAqiData(int month) {
        Map<String, String> data = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT city_name, confirm_level " +
                    "FROM aqifeedback WHERE SUBSTRING(date_feedback, 6, 2) = ? AND SUBSTRING(date_feedback, 9, 2) = '01'";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.format("%02d", month));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String cityName = rs.getString("city_name");
                String confirmLevel = rs.getString("confirm_level");

                data.put(cityName, confirmLevel);
            }
        } catch (SQLException e) {
            System.err.println("从数据库获取AQI数据失败：" + e.getMessage());
            e.printStackTrace();
            JavafxUtil.showAlert(null, "数据获取失败", "无法从数据库加载AQI数据", "请检查数据库连接或数据。", "warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        return data;
    }
}