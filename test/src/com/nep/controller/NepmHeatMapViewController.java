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

    private Map<String, Polygon> cityPolygons = new HashMap<>();
    private Map<String, String> aqiLevelColors = new HashMap<>();
    private Map<String, double[]> simplifiedCityCoordinates = new HashMap<>();
    private Map<String, String> cityToProvinceMap = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("HeatmapViewController: Initializing...");
        for (int i = 1; i <= 12; i++) {
            monthComboBox.getItems().add(i + "月");
        }
        monthComboBox.setValue(java.time.LocalDate.now().getMonthValue() + "月");

        // *** 关键修复：修改 aqiLevelColors 的键，使其与数据库中的 AQI 等级文本完全匹配 ***
        // 数据库中的 confirm_level 存储的是 "一级", "二级", "三级", "四级", "五级", "六级"
        aqiLevelColors.put("一级", "#02E300");      // 绿色
        aqiLevelColors.put("二级", "#FFFF00");      // 黄色
        aqiLevelColors.put("三级", "#FF7E00");      // 橙色
        aqiLevelColors.put("四级", "#FE0000");      // 红色
        aqiLevelColors.put("五级", "#98004B");      // 紫色
        aqiLevelColors.put("六级", "#7E0123");      // 褐红色
        aqiLevelColors.put("无数据", "#CCCCCC");   // 默认灰色

        // ... (simplifiedCityCoordinates 和 cityToProvinceMap 定义不变) ...

        drawBaseMap();
        System.out.println("HeatmapViewController: Initialization complete.");
    

        // *** 再次强调：这里是示例坐标，你需要替换成真实的简化坐标！ ***
        // 辽宁省
        simplifiedCityCoordinates.put("沈阳市", new double[]{400, 200, 450, 210, 440, 250, 390, 240});
        simplifiedCityCoordinates.put("大连市", new double[]{480, 350, 520, 360, 510, 400, 470, 390});
        simplifiedCityCoordinates.put("鞍山市", new double[]{420, 260, 460, 270, 450, 300, 410, 290});
        simplifiedCityCoordinates.put("抚顺市", new double[]{430, 150, 470, 160, 460, 190, 420, 180});
        cityToProvinceMap.put("沈阳市", "辽宁省");
        cityToProvinceMap.put("大连市", "辽宁省");
        cityToProvinceMap.put("鞍山市", "辽宁省");
        cityToProvinceMap.put("抚顺市", "辽宁省");

        // 吉林省
        simplifiedCityCoordinates.put("长春市", new double[]{300, 100, 350, 110, 340, 150, 290, 140});
        simplifiedCityCoordinates.put("吉林市", new double[]{360, 80, 400, 90, 390, 130, 350, 120});
        simplifiedCityCoordinates.put("四平市", new double[]{250, 160, 290, 170, 280, 200, 240, 190});
        simplifiedCityCoordinates.put("公主岭市", new double[]{280, 180, 320, 190, 310, 220, 270, 210});
        cityToProvinceMap.put("长春市", "吉林省");
        cityToProvinceMap.put("吉林市", "吉林省");
        cityToProvinceMap.put("四平市", "吉林省");
        cityToProvinceMap.put("公主岭市", "吉林省");

        // 黑龙江省
        simplifiedCityCoordinates.put("哈尔滨市", new double[]{200, 20, 250, 30, 240, 70, 190, 60});
        simplifiedCityCoordinates.put("大庆市", new double[]{150, 80, 190, 90, 180, 120, 140, 110});
        simplifiedCityCoordinates.put("齐齐哈尔市", new double[]{100, 10, 140, 20, 130, 60, 90, 50});
        cityToProvinceMap.put("哈尔滨市", "黑龙江省");
        cityToProvinceMap.put("大庆市", "黑龙江省");
        cityToProvinceMap.put("齐齐哈尔市", "黑龙江省");

        drawBaseMap();
        System.out.println("HeatmapViewController: Initialization complete.");
    }

    private void drawBaseMap() {
        mapPane.getChildren().clear();
        cityPolygons.clear();
        System.out.println("HeatmapViewController: Drawing base map for " + simplifiedCityCoordinates.size() + " cities.");

        for (Map.Entry<String, double[]> entry : simplifiedCityCoordinates.entrySet()) {
            String cityName = entry.getKey();
            double[] points = entry.getValue();

            Polygon polygon = new Polygon();
            List<Double> doublePoints = Arrays.stream(points).boxed().collect(Collectors.toList());
            polygon.getPoints().addAll(doublePoints);

            polygon.setFill(Color.web(aqiLevelColors.get("无数据")));
            polygon.setStroke(Color.DARKGRAY);
            polygon.setStrokeWidth(1);

            Tooltip tooltip = new Tooltip(cityName + "\nAQI: 无数据");
            Tooltip.install(polygon, tooltip);

            cityPolygons.put(cityName, polygon);
            mapPane.getChildren().add(polygon);
            System.out.println("  - Added polygon for city: " + cityName);
        }
    }

    @FXML
    public void showHeatmap() {
        String selectedMonthText = monthComboBox.getValue();
        if (selectedMonthText == null || selectedMonthText.isEmpty() || selectedMonthText.equals("选择月份")) {
            JavafxUtil.showAlert(null, "选择月份", "请选择一个月份", "请从下拉框中选择要显示热力图的月份。", "warn");
            return;
        }

        int month = Integer.parseInt(selectedMonthText.replace("月", ""));
        System.out.println("HeatmapViewController: Showing heatmap for month: " + month);

        drawBaseMap(); // 重新绘制底图，清除旧的颜色和 Tooltip

        Map<String, String> cityAqiLevels = getMonthlyAqiData(month);
        System.out.println("HeatmapViewController: Data fetched from DB: " + cityAqiLevels.size() + " entries.");
        System.out.println("  Fetched Data: " + cityAqiLevels); // 打印从数据库获取的原始数据

        for (Map.Entry<String, String> entry : cityAqiLevels.entrySet()) {
            String cityName = entry.getKey();
            String aqiLevel = entry.getValue();

            // *** 关键修复：对从数据库获取的 aqiLevel 进行 trim() 操作，去除可能存在的空格 ***
            String trimmedAqiLevel = aqiLevel.trim();

            Polygon polygon = cityPolygons.get(cityName);
            System.out.println("  - Processing city: " + cityName + ", Raw AQI Level: '" + aqiLevel + "', Trimmed AQI Level: '" + trimmedAqiLevel + "'. Polygon found: " + (polygon != null));

            if (polygon != null) {
                String colorHex = aqiLevelColors.get(trimmedAqiLevel); // 使用 trimmed 后的字符串作为键
                System.out.println("    - Mapped color for Trimmed AQI Level '" + trimmedAqiLevel + "': " + colorHex);

                if (colorHex != null) {
                    polygon.setFill(Color.web(colorHex));
                    Tooltip.install(polygon, new Tooltip(cityName + "\nAQI: " + trimmedAqiLevel));
                    System.out.println("      - Successfully colored " + cityName + " with " + colorHex);
                } else {
                    System.out.println("      - WARNING: No color found for Trimmed AQI Level '" + trimmedAqiLevel + "'. Polygon for " + cityName + " remains default.");
                }
            } else {
                System.out.println("    - WARNING: Polygon for city '" + cityName + "' not found in mapPane.");
            }
        }

        JavafxUtil.showAlert(null, "热力图更新", "污染热力图已更新", selectedMonthText + "的污染数据已加载并显示。", "info");
    }

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
                // *** 关键修复：对从数据库获取的 city_name 也进行 trim() 操作 ***
                data.put(cityName.trim(), confirmLevel.trim()); // 确保键值都是trim()过的
                System.out.println("  DB Fetch: '" + cityName + "' -> '" + confirmLevel + "'");
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