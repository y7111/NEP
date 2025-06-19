package com.nep.controller;

import com.nep.entity.AqiFeedback;
import com.nep.entity.GridMember;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.AqiFeedbackService;
import com.nep.service.impl.AqiFeedbackServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NepmAqiAssignViewController implements Initializable {
    @FXML
    private Pane txt_pane1;
    @FXML
    private Pane txt_pane2;
    @FXML
    private Pane txt_pane3;
    @FXML
    private TextField txt_afId;
    @FXML
    private Label label_afId;
    @FXML
    private Label  label_afName;
    @FXML
    private Label  label_proviceName;
    @FXML
    private Label  label_cityName;
    @FXML
    private Label  label_address;
    @FXML
    private Label  label_infomation; // 对应 Java 实体类中的 information 字段
    @FXML
    private Label  label_estimateGrade;
    @FXML
    private Label  label_date; // 对应 Java 实体类中的 dateFeedback 字段
    @FXML
    private ComboBox<String> combo_realName;
    public static Stage aqiInfoStage;
    private AqiFeedbackService aqiFeedbackService = new AqiFeedbackServiceImpl();

    public Pane getTxt_pane1() { return txt_pane1; }
    public void setTxt_pane1(Pane txt_pane1) { this.txt_pane1 = txt_pane1; }
    public Pane getTxt_pane2() { return txt_pane2; }
    public void setTxt_pane2(Pane txt_pane2) { this.txt_pane2 = txt_pane2; }
    public Pane getTxt_pane3() { return txt_pane3; }
    public void setTxt_pane3(Pane txt_pane3) { this.txt_pane3 = txt_pane3; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txt_pane1.setStyle("-fx-border-color: #CCC;");
        txt_pane2.setStyle("-fx-background-color: #CCC;");
        txt_pane3.setStyle("-fx-border-color: #CCC;");

        initConroller();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT real_name FROM grid_member WHERE state = '工作中'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                combo_realName.getItems().add(rs.getString("real_name"));
            }
        } catch (SQLException e) {
            System.err.println("加载网格员数据失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(aqiInfoStage, "数据加载失败", "无法加载网格员信息", "请检查数据库连接或网格员数据","warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
    }

    public void queryFeedback(){
        String afId = txt_afId.getText();
        if (afId == null || afId.trim().isEmpty()) {
            JavafxUtil.showAlert(aqiInfoStage, "查询失败", "请输入反馈信息编号", "反馈信息编号不能为空","warn");
            initConroller();
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean found = false;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT af_id, af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state FROM aqifeedback WHERE af_id = ? AND state = '未指派'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(afId));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                found = true;
                label_afId.setText(rs.getString("af_id"));
                label_afName.setText(rs.getString("af_name"));
                label_address.setText(rs.getString("address"));
                label_cityName.setText(rs.getString("city_name"));
                label_date.setText(rs.getString("date_feedback")); // 修复这里
                label_estimateGrade.setText(rs.getString("estimate_grade"));
                label_infomation.setText(rs.getString("information")); // 修复这里
                label_proviceName.setText(rs.getString("provice_name"));
            }
        } catch (NumberFormatException e) {
            System.err.println("反馈信息编号格式错误：" + afId);
            JavafxUtil.showAlert(aqiInfoStage, "数据格式错误", "反馈信息编号必须是数字", "请重新输入有效的编号","warn");
        } catch (SQLException e) {
            System.err.println("查询反馈信息失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(aqiInfoStage, "数据查询失败", "无法查询反馈信息", "请检查数据库连接或数据","warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }

        if(!found){
            JavafxUtil.showAlert(aqiInfoStage, "查询失败", "未找到当前编号的未指派反馈信息", "请重新输入AQI反馈数据编号","warn");
            initConroller();
        }
    }

    public void assignGridMember(){
        if(label_afId.getText().equals("无")){
            JavafxUtil.showAlert(aqiInfoStage, "指派失败", "未找到要指派的反馈信息", "请选择要指派的反馈信息","warn");
            return;
        }
        if(combo_realName.getValue() == null || combo_realName.getValue().equals("请选择网格员")){
            JavafxUtil.showAlert(aqiInfoStage, "指派失败", "您没有选择要指派的网格员", "请选择您要指派的网格员","warn");
            return;
        }
        String afId = label_afId.getText();
        aqiFeedbackService.assignGridMember(afId, combo_realName.getValue());
        JavafxUtil.showAlert(aqiInfoStage, "指派成功", "AQI反馈信息指派成功!", "请等待网格员实测数据信息","info");
        initConroller();
    }

    public void initConroller(){
        txt_afId.setText("");
        label_afId.setText("无");
        label_afName.setText("无");
        label_address.setText("无");
        label_cityName.setText("无");
        label_date.setText("无");
        label_estimateGrade.setText("无");
        label_infomation.setText("无"); // 修复这里
        label_proviceName.setText("无");
        combo_realName.setValue("请选择网格员");
    }
}