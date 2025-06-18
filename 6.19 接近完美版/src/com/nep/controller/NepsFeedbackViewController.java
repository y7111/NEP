package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.AqiFeedback;
import com.nep.entity.Supervisor;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.util.JavafxUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NepsFeedbackViewController implements Initializable {
    @FXML
    private TableView<AqiFeedback> txt_tableView;
    @FXML
    private Label txt_realName;
    public static Stage primaryStage;
    public static Supervisor supervisor;

    public TableView<AqiFeedback> getTxt_tableView() { return txt_tableView; }
    public void setTxt_tableView(TableView<AqiFeedback> txt_tableView) { this.txt_tableView = txt_tableView; }
    public Label getTxt_realName() { return txt_realName; }
    public void setTxt_realName(Label txt_realName) { this.txt_realName = txt_realName; }

    public void back(){
        JavafxUtil.showStage(NepsMain.class,"view/NepsSelectAqiView.fxml", primaryStage,"东软环保公众监督平台-公众监督员端-AQI数据反馈");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. 检查 supervisor 是否为 null
        if (supervisor == null || supervisor.getRealName() == null || supervisor.getRealName().isEmpty()) {
            txt_realName.setText("未知用户 (请登录)");
            JavafxUtil.showAlert(primaryStage, "会话过期或未登录", "请先登录公众监督员账号", "您需要登录才能查看反馈列表。", "warn");
            // 建议回到登录界面，防止后续操作出现更多问题
            JavafxUtil.showStage(NepsMain.class,"view/NepsLoginViewa.fxml", primaryStage,"东软环保公众监督平台-公众监督员端");
            return; // 立即返回，不执行后续代码
        }
        txt_realName.setText(supervisor.getRealName()); // 如果不为 null，则设置用户名
        // --- 修复部分结束 ---


        //初始化table 数据表
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
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFeedback")); // 修复这里

        TableColumn<AqiFeedback, String> infoColumn = new TableColumn<>("反馈信息");
        infoColumn.setMinWidth(330);
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("information")); // 修复这里

        txt_tableView.getColumns().addAll(afIdColumn, proviceNameColumn,cityNameColumn,estimateGradeColumn,dateColumn,infoColumn);

        ObservableList<AqiFeedback> data = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT af_id, af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state, gm_name, confirm_date, so2, co, pm, confirm_level, confirm_explain FROM aqifeedback WHERE af_name = ? ORDER BY date_feedback DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, supervisor.getRealName()); // 使用 supervisor.getRealName()
            rs = pstmt.executeQuery();

            while (rs.next()) {
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
            System.err.println("加载AQI反馈数据失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(primaryStage, "数据加载失败", "无法加载AQI反馈信息", "请检查数据库连接或反馈数据","warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        txt_tableView.setItems(data);
    }
}