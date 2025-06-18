package com.nep.controller;

import com.nep.entity.AqiFeedback;
import com.nep.DB.DBUtil; // 导入 DBUtil
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Button; // 新增导入
import javafx.stage.FileChooser; // 新增导入
import java.io.File; // 新增导入
import java.io.FileWriter; // 新增导入
import java.io.IOException; // 新增导入
import javafx.stage.Stage; // 新增导入
import com.nep.util.JavafxUtil; // 导入 JavafxUtil

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NepmAqiInfoViewController implements Initializable {

    @FXML
    private TableView<AqiFeedback> txt_tableView;
    @FXML
    private Button exportAqiInfoButton; // 声明 FXML 按钮

    public TableView<AqiFeedback> getTxt_tableView() { return txt_tableView; }
    public void setTxt_tableView(TableView<AqiFeedback> txt_tableView) { this.txt_tableView = txt_tableView; }
    // Getter/Setter for the new export button
    public Button getExportAqiInfoButton() { return exportAqiInfoButton; }
    public void setExportAqiInfoButton(Button exportAqiInfoButton) { this.exportAqiInfoButton = exportAqiInfoButton; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 表格列初始化代码 (保持与你现有的一致)
        TableColumn<AqiFeedback, Integer> afIdColumn = new TableColumn<>("编号");
        afIdColumn.setMinWidth(40);
        afIdColumn.setStyle("-fx-alignment: center;");
        afIdColumn.setCellValueFactory(new PropertyValueFactory<>("afId"));

        TableColumn<AqiFeedback, String> proviceNameColumn = new TableColumn<>("省区域");
        proviceNameColumn.setMinWidth(60);
        proviceNameColumn.setStyle("-fx-alignment: center;");
        proviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("proviceName"));

        TableColumn<AqiFeedback, String> cityNameColumn = new TableColumn<>("市区域");
        cityNameColumn.setMinWidth(60);
        cityNameColumn.setStyle("-fx-alignment: center;");
        cityNameColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));

        TableColumn<AqiFeedback, String> estimateGradeColumn = new TableColumn<>("预估等级");
        estimateGradeColumn.setMinWidth(60);
        estimateGradeColumn.setStyle("-fx-alignment: center;");
        estimateGradeColumn.setCellValueFactory(new PropertyValueFactory<>("estimateGrade"));

        TableColumn<AqiFeedback, String> dateColumn = new TableColumn<>("反馈时间");
        dateColumn.setMinWidth(80);
        dateColumn.setStyle("-fx-alignment: center;");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFeedback")); // 注意这里是 dateFeedback

        TableColumn<AqiFeedback, String> afNameColumn = new TableColumn<>("反馈者");
        afNameColumn.setMinWidth(60);
        afNameColumn.setStyle("-fx-alignment: center;");
        afNameColumn.setCellValueFactory(new PropertyValueFactory<>("afName"));

        TableColumn<AqiFeedback, String> infoColumn = new TableColumn<>("反馈信息");
        infoColumn.setMinWidth(210);
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("information"));

        txt_tableView.getColumns().addAll(afIdColumn, proviceNameColumn,cityNameColumn,estimateGradeColumn,dateColumn,afNameColumn,infoColumn);

        // 从数据库加载未指派的 AQI 反馈数据
        ObservableList<AqiFeedback> data = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = com.nep.DB.DBUtil.getConnection(); // 使用完整的包名来引用 DBUtil
            String sql = "SELECT af_id, af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state, gm_name, confirm_date, so2, co, pm, confirm_level, confirm_explain FROM aqifeedback WHERE state = '未指派'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()){
                AqiFeedback afb = new AqiFeedback();
                afb.setAfId(rs.getInt("af_id"));
                afb.setAfName(rs.getString("af_name"));
                afb.setProviceName(rs.getString("provice_name"));
                afb.setCityName(rs.getString("city_name"));
                afb.setAddress(rs.getString("address"));
                afb.setInformation(rs.getString("information"));
                afb.setEstimateGrade(rs.getString("estimate_grade"));
                afb.setDateFeedback(rs.getString("date_feedback"));
                afb.setState(rs.getString("state"));
                afb.setGmName(rs.getString("gm_name"));
                afb.setConfirmDate(rs.getString("confirm_date"));
                afb.setSo2(rs.getDouble("so2"));
                afb.setCo(rs.getDouble("co"));
                afb.setPm(rs.getDouble("pm"));
                afb.setConfirmLevel(rs.getString("confirm_level"));
                afb.setConfirmExplain(rs.getString("confirm_explain"));
                data.add(afb);
            }
        } catch (SQLException e) {
            System.err.println("加载未指派AQI数据失败：数据库操作异常。");
            e.printStackTrace();
        } finally {
            com.nep.DB.DBUtil.close(rs, pstmt, conn); // 使用完整的包名来引用 DBUtil
        }
        txt_tableView.setItems(data);
    }

    // handleExportAqiInfo 方法 (处理导出按钮点击事件)
    @FXML // FXML 绑定方法
    public void handleExportAqiInfo() {
        // 获取当前舞台，用于文件选择器
        Stage stage = (Stage) txt_tableView.getScene().getWindow();

        // 1. 创建文件选择器
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存反馈数据为 CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("反馈数据.csv"); // 默认文件名

        // 2. 显示保存对话框
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // 3. 写入 CSV 头部 (列名)
                List<String> header = new ArrayList<>();
                // 确保这里的列名顺序与你希望导出的数据顺序匹配
                header.add("编号");
                header.add("省区域");
                header.add("市区域");
                header.add("预估等级");
                header.add("反馈时间");
                header.add("反馈者");
                header.add("反馈信息");
                writer.append(String.join(",", header)); // 用逗号连接列名
                writer.append("\n"); // 换行

                // 4. 写入 CSV 数据行
                ObservableList<AqiFeedback> data = txt_tableView.getItems(); // 获取表格中当前显示的所有数据
                for (AqiFeedback item : data) {
                    List<String> rowData = new ArrayList<>();
                    // 按照头部定义的顺序，获取 AqiFeedback 对象的对应属性值
                    // 注意对可能包含逗号或引号的字符串进行处理 (例如，加双引号并转义内部双引号)
                    rowData.add(String.valueOf(item.getAfId()));
                    rowData.add(item.getProviceName());
                    rowData.add(item.getCityName());
                    rowData.add(item.getEstimateGrade());
                    rowData.add(item.getDateFeedback());
                    rowData.add(item.getAfName());
                    rowData.add("\"" + item.getInformation().replace("\"", "\"\"") + "\""); // 处理信息字段中的逗号和引号

                    writer.append(String.join(",", rowData));
                    writer.append("\n");
                }

                // 5. 提示用户导出成功
                JavafxUtil.showAlert(stage, "导出成功", "反馈数据已成功导出！", "文件路径：" + file.getAbsolutePath(), "info");
            } catch (IOException e) {
                // 6. 捕获并处理文件写入异常
                System.err.println("导出文件失败：" + e.getMessage());
                JavafxUtil.showAlert(stage, "导出失败", "无法保存文件", "请检查文件权限或重试。", "warn");
                e.printStackTrace();
            }
        }
    }
}