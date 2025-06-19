package com.nep.controller;

import com.nep.NepmMain; // 确保导入 NepmMain
import com.nep.util.JavafxUtil; // 确保导入 JavafxUtil
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NepmMainViewController implements Initializable {
    public static Stage primaryStage;
    @FXML
    private ImageView txt_imageView;

    public ImageView getTxt_imageView() { return txt_imageView; }
    public void setTxt_imageView(ImageView txt_imageView) { this.txt_imageView = txt_imageView; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image image = new Image(getClass().getResourceAsStream("/image/welcome.png"));

        txt_imageView.setImage(image);
        txt_imageView.setPreserveRatio(false);
    }

    // AQI反馈数据查询（未指派）
    public void aqiInfo(){
        JavafxUtil.showSubStage(NepmMain.class, "view/NepmAqiInfoView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI反馈数据查询");
    }

    // AQI反馈数据指派
    public void aqiAssign(){
        NepmAqiAssignViewController.aqiInfoStage = JavafxUtil.showSubStage(NepmMain.class, "view/NepmAqiAssignView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI反馈数据指派");
    }

    // AQI确认数据查询
    public void aqiConfirm(){
        JavafxUtil.showSubStage(NepmMain.class, "view/NepmConfirmInfoView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI实测数据查询");
    }

    // 处理“公众监督员端”->“导出反馈数据”菜单项
    public void exportFeedbackData(){
        JavafxUtil.showSubStage(NepmMain.class, "view/NepmAqiInfoView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI反馈数据查询（导出）");
    }

    // 处理“网格员端”->“导出实测数据”菜单项
    public void exportConfirmedData(){
        JavafxUtil.showSubStage(NepmMain.class, "view/NepmConfirmInfoView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI实测数据查询（导出）");
    }

    // 处理“AQI信息统计”->“污染热力图”菜单项
    @FXML // FXML 绑定方法
    public void showPollutionHeatmap(){
        // 弹出 HeatmapView.fxml 窗口
        JavafxUtil.showSubStage(NepmMain.class, "view/heatmap.fxml", primaryStage, "东软环保公众监督平台-管理端-污染热力图");
    }

    // 在 com.nep.controller.NepmMainViewController 类中添加
    @FXML
    public void showPollutionChart(){
        // 假设您的 primaryStage 是 NepmMainViewController 的静态变量或者通过其他方式传递
        JavafxUtil.showSubStage(NepmMain.class, "view/NepmPollutionChartView.fxml", primaryStage, "东软环保公众监督平台-管理端-AQI污染统计图");
    }
}