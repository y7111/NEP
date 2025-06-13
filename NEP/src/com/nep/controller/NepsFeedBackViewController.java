package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.AqiFeedback;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class NepsFeedBackViewController implements Initializable {


    public static Stage primaryStage;

    @FXML
    private TableView<AqiFeedback> txt_tableView;

    @FXML
    private Label txt_realName;

    public TableView<AqiFeedback> getTxt_tableView() {
        return txt_tableView;
    }

    public void setTxt_tableView(TableView<AqiFeedback> txt_tableView) {
        this.txt_tableView = txt_tableView;
    }

    public Label getTxt_realName() {
        return txt_realName;
    }

    public void setTxt_realName(Label txt_realName) {
        this.txt_realName = txt_realName;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void back(){

        JavafxUtil.showStage(NepsMain.class,"view/NepsFeedBackView.fxml",primaryStage,"东软环保公众监督员-Aqi反馈");
    }
}
