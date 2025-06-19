package com.nep;

import com.nep.controller.NepsLoginViewController;
import com.nep.util.JavafxUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NepsMain extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        NepsLoginViewController.primaryStage = primaryStage;
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
