package com.nep.controller;

import com.nep.dto.AqiLimitDto;
import com.nep.entity.AqiFeedback;
import com.nep.entity.GridMember;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NepgAqiConfirmViewController implements Initializable {
    @FXML
    private TableView<AqiFeedback> txt_tableView;
    @FXML
    private Pane txt_pane;
    @FXML
    private TextField txt_afId;
    @FXML
    private TextField txt_so2;
    @FXML
    private TextField txt_co;
    @FXML
    private TextField txt_pm;
    @FXML
    private Label label_so2level;
    @FXML
    private Label label_so2explain;
    @FXML
    private Label label_colevel;
    @FXML
    private Label label_coexplain;
    @FXML
    private Label label_pmlevel;
    @FXML
    private Label label_pmexplain;
    @FXML
    private Label label_confirmlevel;
    @FXML
    private Label label_confirmexplain;
    @FXML
    private Label label_realName;
    public static GridMember gridMember;
    public static Stage primaryStage;
    private AqiFeedbackService aqiFeedbackService = new AqiFeedbackServiceImpl();
    private int so2level;
    private int colevel;
    private int pmlevel;
    private AqiLimitDto confirmDto;

    public TableView<AqiFeedback> getTxt_tableView() { return txt_tableView; }
    public void setTxt_tableView(TableView<AqiFeedback> txt_tableView) { this.txt_tableView = txt_tableView; }
    public Pane getTxt_pane() { return txt_pane; }
    public void setTxt_pane(Pane txt_pane) { this.txt_pane = txt_pane; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txt_pane.setStyle("-fx-border-color: #CCC;");

        if (gridMember != null) {
            label_realName.setText(gridMember.getRealName());
        } else {
            label_realName.setText("未知用户");
        }

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
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFeedback"));

        TableColumn<AqiFeedback, String> afNameColumn = new TableColumn<>("反馈者");
        afNameColumn.setMinWidth(60);
        afNameColumn.setStyle("-fx-alignment: center;");
        afNameColumn.setCellValueFactory(new PropertyValueFactory<>("afName"));

        TableColumn<AqiFeedback, String> addressColumn = new TableColumn<>("具体地址");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<AqiFeedback, String> infoColumn = new TableColumn<>("反馈信息");
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("information"));

        txt_tableView.getColumns().addAll(afIdColumn,afNameColumn,dateColumn,estimateGradeColumn, proviceNameColumn,cityNameColumn,addressColumn,infoColumn);

        ObservableList<AqiFeedback> data = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT af_id, af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state, gm_name, confirm_date, so2, co, pm, confirm_level, confirm_explain FROM aqifeedback WHERE gm_name = ? AND state = '已指派'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gridMember.getRealName());
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
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        txt_tableView.setItems(data);

        txt_afId.focusedProperty().addListener((obs,wasFocused,isNowFocused)->{
            if(!isNowFocused && !txt_afId.getText().isEmpty()){
                boolean found = false;
                for(AqiFeedback afbFromTable : data){
                    if(afbFromTable.getAfId().toString().equals(txt_afId.getText())){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    JavafxUtil.showAlert(primaryStage, "数据错误", "AQI反馈数据编号有误", "请重新输入AQI反馈数据编号","warn");
                    txt_afId.setText("");
                }
            }
        });

        txt_so2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!txt_so2.getText().equals("")){
                    AqiLimitDto dto = CommonUtil.so2Limit(Double.parseDouble(txt_so2.getText()));
                    label_so2level.setText(dto.getLevel());
                    label_so2level.setStyle("-fx-text-fill:"+dto.getColor()+";");
                    label_so2explain.setText(dto.getExplain());
                    label_so2explain.setStyle("-fx-background-color:"+dto.getColor()+";");
                    so2level = dto.getIntlevel();
                    confirmDto = CommonUtil.confirmLevel(so2level, colevel, pmlevel);
                    label_confirmlevel.setText(confirmDto.getLevel());
                    label_confirmlevel.setStyle("-fx-text-fill:"+confirmDto.getColor()+";");
                    label_confirmexplain.setText(confirmDto.getExplain());
                    label_confirmexplain.setStyle("-fx-background-color:"+confirmDto.getColor()+";");
                }
            }
        });
        txt_co.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!txt_co.getText().equals("")){
                    AqiLimitDto dto = CommonUtil.coLimit(Double.parseDouble(txt_co.getText()));
                    label_colevel.setText(dto.getLevel());
                    label_colevel.setStyle("-fx-text-fill:"+dto.getColor()+";");
                    label_coexplain.setText(dto.getExplain());
                    label_coexplain.setStyle("-fx-background-color:"+dto.getColor()+";");
                    colevel = dto.getIntlevel();
                    confirmDto = CommonUtil.confirmLevel(so2level, colevel, pmlevel);
                    label_confirmlevel.setText(confirmDto.getLevel());
                    label_confirmlevel.setStyle("-fx-text-fill:"+confirmDto.getColor()+";");
                    label_confirmexplain.setText(confirmDto.getExplain());
                    label_confirmexplain.setStyle("-fx-background-color:"+confirmDto.getColor()+";");
                }
            }
        });
        txt_pm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!txt_pm.getText().equals("")){
                    AqiLimitDto dto = CommonUtil.pmLimit(Double.parseDouble(txt_pm.getText()));
                    label_pmlevel.setText(dto.getLevel());
                    label_pmlevel.setStyle("-fx-text-fill:"+dto.getColor()+";");
                    label_pmexplain.setText(dto.getExplain());
                    label_pmexplain.setStyle("-fx-background-color:"+dto.getColor()+";");
                    pmlevel = dto.getIntlevel();
                    confirmDto = CommonUtil.confirmLevel(so2level, colevel, pmlevel);
                    label_confirmlevel.setText(confirmDto.getLevel());
                    label_confirmlevel.setStyle("-fx-text-fill:"+confirmDto.getColor()+";");
                    label_confirmexplain.setText(confirmDto.getExplain());
                    label_confirmexplain.setStyle("-fx-background-color:"+confirmDto.getColor()+";");
                }
            }
        });
    }

    public void confirmData(){
        AqiFeedback afb = new AqiFeedback();
        afb.setAfId(Integer.parseInt(txt_afId.getText()));
        afb.setState("已实测");
        afb.setSo2(Double.parseDouble(txt_so2.getText()));
        afb.setCo(Double.parseDouble(txt_co.getText()));
        afb.setPm(Double.parseDouble(txt_pm.getText()));
        afb.setConfirmDate(CommonUtil.currentDate());
        afb.setConfirmLevel(confirmDto.getLevel());
        afb.setConfirmExplain(confirmDto.getExplain());
        afb.setGmName(gridMember.getRealName());
        aqiFeedbackService.confirmData(afb);
        JavafxUtil.showAlert(primaryStage, "提交成功", "污染物实测数据提交成功", "","info");

        ObservableList<AqiFeedback> data = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT af_id, af_name, provice_name, city_name, address, information, estimate_grade, date_feedback, state, gm_name, confirm_date, so2, co, pm, confirm_level, confirm_explain FROM aqifeedback WHERE gm_name = ? AND state = '已指派'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gridMember.getRealName());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AqiFeedback a = new AqiFeedback();
                a.setAfId(rs.getInt("af_id"));
                a.setAfName(rs.getString("af_name"));
                a.setProviceName(rs.getString("provice_name"));
                a.setCityName(rs.getString("city_name"));
                a.setAddress(rs.getString("address"));
                a.setInformation(rs.getString("information"));
                a.setEstimateGrade(rs.getString("estimate_grade"));
                a.setDateFeedback(rs.getString("date_feedback"));
                a.setState(rs.getString("state"));
                a.setGmName(rs.getString("gm_name"));
                a.setConfirmDate(rs.getString("confirm_date"));
                a.setSo2(rs.getDouble("so2"));
                a.setCo(rs.getDouble("co"));
                a.setPm(rs.getDouble("pm"));
                a.setConfirmLevel(rs.getString("confirm_level"));
                a.setConfirmExplain(rs.getString("confirm_explain"));
                data.add(a);
            }
        } catch (SQLException e) {
            System.err.println("刷新数据失败：数据库操作异常。");
            e.printStackTrace();
            JavafxUtil.showAlert(primaryStage, "数据刷新失败", "无法刷新AQI反馈信息", "请检查数据库连接","warn");
        } finally {
            DBUtil.close(rs, pstmt, conn);
        }
        txt_tableView.setItems(data);
        reset();
    }

    public void reset(){
        txt_afId.setText("");
        txt_so2.setText("");
        txt_co.setText("");
        txt_pm.setText("");
        label_so2explain.setText("");
        label_so2explain.setStyle("-fx-background-color:none;");
        label_coexplain.setText("");
        label_coexplain.setStyle("-fx-background-color:none;");
        label_pmexplain.setText("");
        label_pmexplain.setStyle("-fx-background-color:none;");
        label_confirmexplain.setText("");
        label_confirmexplain.setStyle("-fx-background-color:none;");
        label_so2level.setText("无");
        label_so2level.setStyle("-fx-text-fill:black;");
        label_colevel.setText("无");
        label_colevel.setStyle("-fx-text-fill:black;");
        label_pmlevel.setText("无");
        label_pmlevel.setStyle("-fx-text-fill:black;");
        label_confirmlevel.setText("无");
        label_confirmlevel.setStyle("-fx-text-fill:black;");
    }
}