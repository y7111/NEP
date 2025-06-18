package com.nep.controller;

import com.nep.DB.DBUtil;
import com.nep.dto.AqiLimitDto; // 导入 AqiLimitDto，虽然当前版本未直接使用，但可能用于其他AQI相关逻辑
import com.nep.entity.ProvinceCity; // 导入 ProvinceCity，如果后续需要获取省份信息
import com.nep.util.CommonUtil; // 导入 CommonUtil，如果需要使用其中AQI等级判断方法
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.collections.FXCollections;

import javafx.util.StringConverter; // 新增导入

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class NepmPollutionChartController implements Initializable {

    @FXML
    private ComboBox<String> citySelectionComboBox; // 城市选择下拉框
    @FXML
    private LineChart<String, Number> aqiLevelLineChart; // AQI等级变化折线图
    @FXML
    private CategoryAxis xAxis; // 折线图X轴 (月份)
    @FXML
    private NumberAxis yAxis; // 折线图Y轴 (AQI等级)

    // AQI等级到数值的映射，用于折线图
    private static final Map<String, Integer> AQI_LEVEL_TO_NUMERIC = new LinkedHashMap<>();
    static {
        AQI_LEVEL_TO_NUMERIC.put("优", 1);
        AQI_LEVEL_TO_NUMERIC.put("良", 2);
        AQI_LEVEL_TO_NUMERIC.put("轻度污染", 3);
        AQI_LEVEL_TO_NUMERIC.put("中度污染", 4);
        AQI_LEVEL_TO_NUMERIC.put("重度污染", 5);
        AQI_LEVEL_TO_NUMERIC.put("严重污染", 6);
        // 数据库中存储的可能是 "一级", "二级" 等
        AQI_LEVEL_TO_NUMERIC.put("一级", 1);
        AQI_LEVEL_TO_NUMERIC.put("二级", 2);
        AQI_LEVEL_TO_NUMERIC.put("三级", 3);
        AQI_LEVEL_TO_NUMERIC.put("四级", 4);
        AQI_LEVEL_TO_NUMERIC.put("五级", 5);
        AQI_LEVEL_TO_NUMERIC.put("六级", 6);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("NepmPollutionChartController: Initializing...");

        // 初始化折线图的X轴和Y轴
        initializeAqiLineChartAxes();
        // 填充城市选择下拉框
        populateCitySelection();

        // 为城市选择下拉框添加监听器
        citySelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !newValue.equals("选择城市")) {
                updateAqiLineChart(newValue);
            } else {
                aqiLevelLineChart.getData().clear(); // 清空图表数据
            }
        });

        System.out.println("NepmPollutionChartController: Initialization complete.");
    }

    /**
     * 初始化 AQI 等级变化折线图的坐标轴
     */
    private void initializeAqiLineChartAxes() {
        if (xAxis == null || yAxis == null) {
            System.err.println("Line Chart Axes FXML elements not injected correctly.");
            return;
        }

        // 配置Y轴 (AQI等级)
        yAxis.setLabel("AQI 等级");
        yAxis.setTickUnit(1);
        yAxis.setLowerBound(0.5); // 确保刻度从1开始清晰显示
        yAxis.setUpperBound(6.5); // 最高等级是6
        yAxis.setMinorTickVisible(false);
        // 自定义Y轴刻度标签，显示AQI等级名称
        yAxis.setTickLabelFormatter(new StringConverter<Number>() { // 修改这一行
            @Override
            public String toString(Number object) {
                // 根据数值返回AQI等级名称
                for (Map.Entry<String, Integer> entry : AQI_LEVEL_TO_NUMERIC.entrySet()) {
                    if (entry.getValue().intValue() == object.intValue()) {
                        return entry.getKey();
                    }
                }
                return ""; // 没有匹配的等级
            }

            @Override
            public Number fromString(String string) {
                // 这个方法在此场景下通常不会被调用，除非用户可以编辑Y轴标签并进行转换
                // 如果需要，可以根据AQI等级名称返回对应的数值
                return AQI_LEVEL_TO_NUMERIC.getOrDefault(string, 0);
            }
        });

        // 配置X轴 (月份)
        xAxis.setLabel("月份");
        // 确保X轴显示所有月份
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i + "月");
        }
        xAxis.setCategories(FXCollections.observableArrayList(months));

        aqiLevelLineChart.setTitle("2024年AQI等级月度变化");
        aqiLevelLineChart.setLegendVisible(false); // 通常单条折线图不需要图例
        aqiLevelLineChart.setCreateSymbols(true); // 显示数据点
    }

    /**
     * 填充城市选择下拉框
     */
    private void populateCitySelection() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set<String> uniqueCities = new TreeSet<>(); // 使用TreeSet保证城市名有序且不重复

        try {
            conn = DBUtil.getConnection();
            // 查询2024年有AQI反馈数据的城市
            String sql = "SELECT DISTINCT city_name FROM aqifeedback WHERE SUBSTRING(date_feedback, 1, 4) = '2024' ORDER BY city_name";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                uniqueCities.add(rs.getString("city_name").trim());
            }
            citySelectionComboBox.getItems().add("选择城市"); // 默认提示项
            citySelectionComboBox.getItems().addAll(uniqueCities);
            citySelectionComboBox.setValue("选择城市");

        } catch (SQLException e) {
            System.err.println("加载城市数据失败：" + e.getMessage());
            e.printStackTrace();
            JavafxUtil.showAlert(null, "数据加载失败", "无法加载城市列表", "请检查数据库连接或数据。", "warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    /**
     * 更新 AQI 等级变化折线图
     * @param selectedCity 选中的城市名称
     */
    private void updateAqiLineChart(String selectedCity) {
        aqiLevelLineChart.getData().clear(); // 清空旧数据

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(selectedCity + " AQI等级变化"); // 系列名称

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // 使用 LinkedHashMap 存储月度数据，确保月份顺序，并记录是否存在数据
        Map<Integer, Integer> monthlyAqiData = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyAqiData.put(i, null); // 初始化为null，表示无数据
        }

        try {
            conn = DBUtil.getConnection();
            // 查询指定城市2024年各个月份的AQI等级数据（只取每月1号的数据）
            String sql = "SELECT SUBSTRING(date_feedback, 6, 2) AS month, confirm_level " +
                    "FROM aqifeedback WHERE city_name = ? AND SUBSTRING(date_feedback, 1, 4) = '2024' " +
                    "AND SUBSTRING(date_feedback, 9, 2) = '01' ORDER BY month";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, selectedCity.trim());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int month = Integer.parseInt(rs.getString("month"));
                String confirmLevel = rs.getString("confirm_level").trim();
                Integer numericLevel = AQI_LEVEL_TO_NUMERIC.get(confirmLevel);
                if (numericLevel != null) {
                    monthlyAqiData.put(month, numericLevel);
                } else {
                    System.err.println("NepmPollutionChartController: 未知AQI等级: " + confirmLevel + "，来自城市 " + selectedCity + "，月份 " + month);
                }
            }

            // 将数据添加到系列中
            for (Map.Entry<Integer, Integer> entry : monthlyAqiData.entrySet()) {
                String monthLabel = entry.getKey() + "月";
                Integer aqiValue = entry.getValue();
                if (aqiValue != null) {
                    XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(monthLabel, aqiValue);
                    series.getData().add(dataPoint);
                    // 为数据点添加 Tooltip 以显示具体等级名称
                    Tooltip.install(dataPoint.getNode(), new Tooltip(monthLabel + "\nAQI: " + AQI_LEVEL_TO_NUMERIC.entrySet().stream()
                            .filter(e -> e.getValue().equals(aqiValue))
                            .map(Map.Entry::getKey)
                            .findFirst().orElse("未知等级")));
                } else {
                    // 如果某个月份没有数据，可以在图上不显示该点。
                    // 如果需要显示缺失点，可以添加：series.getData().add(new XYChart.Data<>(monthLabel, null));
                }
            }

            aqiLevelLineChart.getData().add(series); // 添加系列到图表

        } catch (SQLException e) {
            System.err.println("更新AQI折线图数据失败：" + e.getMessage());
            e.printStackTrace();
            JavafxUtil.showAlert(null, "图表数据获取失败", "无法加载选定城市的AQI月度数据", "请检查数据库连接或数据。", "warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }
}