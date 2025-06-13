package com.nep;

import com.nep.controller.NepsLoginViewController;
import com.nep.util.JavafxUtil;
import javafx.application.Application;
import javafx.stage.Stage;

public class NepsMain extends Application {
    /**
     * 重写启动方法，会在主方法调用lauch的方法
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        NepsLoginViewController.primaryStage=primaryStage;
        JavafxUtil.showStage(NepsMain.class, "/com/nep/view/NepsLoginView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
//将主舞台传递


    }

    public static void main(String[] args) {
        launch(args);
    }
}