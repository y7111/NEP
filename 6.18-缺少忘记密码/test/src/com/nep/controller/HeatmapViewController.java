package com.nep.controller;

import com.nep.DB.DBUtil;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.geometry.Bounds;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HeatmapViewController implements Initializable {

    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private Pane mapPane;

    //hashmap键值对
    private Map<String, Polygon> cityPolygons = new HashMap<>();
    private Map<String, Label> cityLabels = new HashMap<>();
    private Map<String, String> aqiLevelColors = new HashMap<>();
    // simplifiedCityCoordinates  填充坐标
    private Map<String, double[]> simplifiedCityCoordinates = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 填充月份选择框 (1月到12月)
        for (int i = 1; i <= 12; i++) {
            monthComboBox.getItems().add(i + "月");
        }
        monthComboBox.setValue(java.time.LocalDate.now().getMonthValue() + "月"); 

        // 初始化AQI等级到颜色的映射。
        // 这些键必须与数据库aqifeedback表中confirm_level字段存储的实际文本值完全一致。
        aqiLevelColors.put("一级", "#02E300");
        aqiLevelColors.put("二级", "#FFFF00");
        aqiLevelColors.put("三级", "#FF7E00");
        aqiLevelColors.put("四级", "#FE0000");
        aqiLevelColors.put("五级", "#98004B");
        aqiLevelColors.put("六级", "#7E0123");
        aqiLevelColors.put("无数据", "#CCCCCC"); 



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

        drawBaseMap();
    }

    /**
     * 绘制地图底图（所有城市的多边形，并放置城市名称Label）。
     * 多边形默认填充为“无数据”颜色。
     * 每次更新热力图（点击“显示热力图”按钮）前都会调用此方法，以重置颜色和标签状态。
     */
    private void drawBaseMap() {
        //这三行代码保证运行完之后地图画布是一个空的
        mapPane.getChildren().clear();
        cityPolygons.clear();
        cityLabels.clear();
        
        for (Map.Entry<String, double[]> entry : simplifiedCityCoordinates.entrySet()) {
            //遍历键值对
            String cityName = entry.getKey();
            double[] points = entry.getValue();
            //绘制多边形
            Polygon polygon = new Polygon();

            // 这行代码将原始的double[]数组 转换为List<Double>（Double对象列表）。
            // Polygon的getPoints().addAll()方法要求接收的是List<Double>，而不是double[]。


            // - Arrays.stream(points): 将数组转换为一个Stream流。
            // - .boxed(): 将流中的每个double基本类型值装箱成对应的Double对象。
            // - .collect(Collectors.toList()): 将这些Double对象收集成一个新的List，赋值给doublepoints
            List<Double> doublePoints = Arrays.stream(points).boxed().collect(Collectors.toList());
            polygon.getPoints().addAll(doublePoints); 

            //未选择月份之前是无数据的
            polygon.setFill(Color.web(aqiLevelColors.get("无数据")));
            //设置多边形的边框颜色。
            polygon.setStroke(Color.DARKGRAY);
            //绘制多边形边框的粗细。
            polygon.setStrokeWidth(1);

            
            cityPolygons.put(cityName, polygon);
            //在我的pane上添加这个polygon
            mapPane.getChildren().add(polygon);


            //设置标签（黑色）
            Label cityLabel = new Label(cityName);
            cityLabel.setFont(new Font("System Bold", 10));
            cityLabel.setTextFill(Color.BLACK);

            Bounds bounds = polygon.getBoundsInLocal();
            double centerX = bounds.getMinX() + bounds.getWidth() / 2;
            double centerY = bounds.getMinY() + bounds.getHeight() / 2;

            cityLabel.setLayoutX(centerX - cityLabel.prefWidth(-1) / 2);
            cityLabel.setLayoutY(centerY - cityLabel.prefHeight(-1) / 2);

            cityLabels.put(cityName, cityLabel);
            mapPane.getChildren().add(cityLabel);
        }
    }

    /**
     * 显示热力图，根据用户选择的月份从数据库获取AQI数据并着色地图。
     * 此方法绑定到中“显示热力图”按钮的onAction事件。
     */

    public void showHeatmap() {
        String selectedMonthText = monthComboBox.getValue();
        if (selectedMonthText == null || selectedMonthText.isEmpty() || selectedMonthText.equals("选择月份")) {
            JavafxUtil.showAlert(null, "选择月份", "请选择一个月份", "请从下拉框中选择要显示热力图的月份。", "warn");
            return;
        }

        int month = Integer.parseInt(selectedMonthText.replace("月", ""));
        
        drawBaseMap();

        Map<String, String> cityAqiLevels = getMonthlyAqiData(month);

        for (Map.Entry<String, String> entry : cityAqiLevels.entrySet()) {
            String cityName = entry.getKey();
            String aqiLevel = entry.getValue();
            
            String trimmedAqiLevel = aqiLevel.trim(); 
            String trimmedCityName = cityName.trim();

            Polygon polygon = cityPolygons.get(trimmedCityName);
            Label cityLabel = cityLabels.get(trimmedCityName);

            if (polygon != null) {
                String colorHex = aqiLevelColors.get(trimmedAqiLevel);

                if (colorHex != null) {
                    polygon.setFill(Color.web(colorHex));
                    Tooltip.install(polygon, new Tooltip(trimmedCityName + "\nAQI: " + trimmedAqiLevel));
                    
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
            String sql = "SELECT city_name, confirm_level FROM aqifeedback WHERE SUBSTRING(date_feedback, 6, 2) = ? AND SUBSTRING(date_feedback, 9, 2) = '01'"; 
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.format("%02d", month));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String cityName = rs.getString("city_name");
                String confirmLevel = rs.getString("confirm_level");
                data.put(cityName.trim(), confirmLevel.trim()); 
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