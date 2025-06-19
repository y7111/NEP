package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Aqi;
import com.nep.entity.AqiFeedback;
import com.nep.entity.ProvinceCity;
import com.nep.entity.Supervisor;
import com.nep.DB.DBUtil; // 导入 DBUtil
import com.nep.service.AqiFeedbackService;
import com.nep.service.impl.AqiFeedbackServiceImpl;
import com.nep.util.CommonUtil;
import com.nep.util.JavafxUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class NepsSelectAqiViewController implements Initializable {
    @FXML
    private TableView<Aqi> txt_tableView;
    @FXML
    private ComboBox<String> txt_province;
    @FXML
    private ComboBox<String> txt_city;
    @FXML
    private TextField txt_address;
    @FXML
    private ComboBox<String> txt_level;
    @FXML
    private TextArea txt_information;
    @FXML
    private Label label_realName;
    public static Stage primaryStage;
    public static Supervisor supervisor;

    public TableView<Aqi> getTxt_tableView() { return txt_tableView; }
    public void setTxt_tableView(TableView<Aqi> txt_tableView) { this.txt_tableView = txt_tableView; }
    public ComboBox<String> getTxt_province() { return txt_province; }
    public void setTxt_province(ComboBox<String> txt_province) { this.txt_province = txt_province; }
    public ComboBox<String> getTxt_city() { return txt_city; }
    public void setTxt_city(ComboBox<String> txt_city) { this.txt_city = txt_city; }
    public TextField getTxt_address() { return txt_address; }
    public void setTxt_address(TextField txt_address) { this.txt_address = txt_address; }
    public ComboBox<String> getTxt_level() { return txt_level; }
    public void setTxt_level(ComboBox<String> txt_level) { this.txt_level = txt_level; }
    public TextArea getTxt_information() { return txt_information; }
    public void setTxt_information(TextArea txt_information) { this.txt_information = txt_information; }
    public Label getLabel_realName() { return label_realName; }
    public void setLabel_realName(Label label_realName) { this.label_realName = label_realName; }
    public static Stage getPrimaryStage() { return primaryStage; }
    public static void setPrimaryStage(Stage primaryStage) { NepsSelectAqiViewController.primaryStage = primaryStage; }
    public static Supervisor getSupervisor() { return supervisor; }
    public static void setSupervisor(Supervisor supervisor) { NepsSelectAqiViewController.supervisor = supervisor; }

    private AqiFeedbackService aqiFeedbackService = new AqiFeedbackServiceImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (supervisor != null) {
            label_realName.setText(supervisor.getRealName());
        } else {
            label_realName.setText("未知用户");
        }

        TableColumn<Aqi, String> levelColumn = new TableColumn<>("级别");
        levelColumn.setMinWidth(80);
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        TableColumn<Aqi, String> explainColumn = new TableColumn<>("说明");
        explainColumn.setMinWidth(80);
        explainColumn.setCellValueFactory(new PropertyValueFactory<>("explain"));

        TableColumn<Aqi, String> impactColumn = new TableColumn<>("对健康影响");
        impactColumn.setMinWidth(425);
        impactColumn.setCellValueFactory(new PropertyValueFactory<>("impact"));

        txt_tableView.getColumns().addAll(levelColumn, explainColumn, impactColumn);

        ObservableList<Aqi> aqiData = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT level, explain_desc, impact FROM aqi";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                aqiData.add(new Aqi(
                        rs.getString("level"),
                        rs.getString("explain_desc"),
                        rs.getString("impact")
                ));
            }
        } catch (SQLException e) {
            System.err.println("加载AQI标准指数数据失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(primaryStage, "数据加载失败", "无法加载AQI标准指数信息", "请检查数据库连接或AQI数据","warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        txt_tableView.setItems(aqiData);

        for(Aqi aqi : aqiData){
            txt_level.getItems().add(aqi.getLevel());
        }
        txt_level.setValue("预估AQI等级");

        List<ProvinceCity> plist = new ArrayList<>();
        Connection conn2 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;
        try {
            conn2 = DBUtil.getConnection();
            String sql2 = "SELECT province_id, province_name, city_names FROM province_city";
            pstmt2 = conn2.prepareStatement(sql2);
            rs2 = pstmt2.executeQuery();
            while (rs2.next()) {
                plist.add(new ProvinceCity(
                        rs2.getInt("province_id"),
                        rs2.getString("province_name"),
                        rs2.getString("city_names")
                ));
            }
        } catch (SQLException e) {
            System.err.println("加载省市区域数据失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(primaryStage, "数据加载失败", "无法加载省市区域信息", "请检查数据库连接或省市数据","warn");
        } finally {
            DBUtil.close(rs2, pstmt2, conn2);
        }

        for(ProvinceCity province : plist){
            txt_province.getItems().add(province.getProvinceName());
        }
        txt_province.setValue("请选择省区域");
        txt_city.setValue("请选择市区域");

        txt_province.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txt_city.setItems(FXCollections.observableArrayList());
                List<String> clist = new ArrayList<String>();
                for (int i = 0; i < plist.size(); i++) {
                    if(newValue.equals(plist.get(i).getProvinceName())){
                        clist = plist.get(i).getCityName();
                    }
                }
                for(String cityName:clist){
                    txt_city.getItems().add(cityName);
                }
            }
        });
    }

    public void saveFeedBack(){
        AqiFeedback afb = new AqiFeedback();
        afb.setAddress(txt_address.getText());
        afb.setAfName(supervisor.getRealName());
        afb.setProviceName(txt_province.getValue());
        afb.setCityName(txt_city.getValue());
        afb.setEstimateGrade(txt_level.getValue());
        afb.setInformation(txt_information.getText());
        afb.setDateFeedback(CommonUtil.currentDate());
        afb.setState("未指派");
        aqiFeedbackService.saveFeedBack(afb);
        JavafxUtil.showAlert(primaryStage, "反馈信息成功", "您的预估AQI信息提交成功", "感谢您的反馈!","info");
        NepsFeedbackViewController.primaryStage = primaryStage;
        JavafxUtil.showStage(NepsMain.class,"view/NepsFeedbackView.fxml", primaryStage,"东软环保公众监督平台-公众监督员端-AQI反馈数据列表");
    }

    public void feedBackList(){
        NepsFeedbackViewController.primaryStage = primaryStage;

        NepsFeedbackViewController.supervisor = supervisor;
        JavafxUtil.showStage(NepsMain.class,"view/NepsFeedbackView.fxml", primaryStage,"东软环保公众监督平台-公众监督员端-AQI反馈数据列表");
    }
}