package com.nep.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class JavafxUtil {
    /**
     * 配置各类型提示框
     */
    public static void showAlert(Stage primaryStage, String title, String headerText, String contentText, String alertType){
        Alert alert = null;
        switch (alertType) {
            case "warn":
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            case "info":
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            default:
                break;
        }
        //alert的所有者
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        //展示并等待
        alert.showAndWait();
    }

    /**
     * 界面切换函数
     */
    public static void showStage(Class clazz,String path,Stage primaryStage,String title){


        FXMLLoader loader = new FXMLLoader();
        URL url = clazz.getResource(path);
        loader.setLocation(url);
        try {
            //不固定布局
            AnchorPane pane = loader.load();
            //创建画布
            Scene scene = new Scene(pane);
            //将画布绑定给舞台信息
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 主界面切换子界面
     */
    public static Stage showSubStage(Class clazz,String path,Stage primaryStage,String title){
        FXMLLoader loader = new FXMLLoader();
        URL url = clazz.getResource(path);
        loader.setLocation(url);
        Stage subStage = new Stage();
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            subStage.setTitle(title);
            subStage.setScene(scene);
            subStage.initOwner(primaryStage);
            subStage.initModality(Modality.WINDOW_MODAL);
            subStage.showAndWait();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return subStage;
    }
}