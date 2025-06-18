package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NepsForgotPasswordAnswerController {
    @FXML
    private Label lbl_securityQuestion;
    @FXML
    private TextField txt_securityAnswer;

    // 主舞台
    public static Stage primaryStage;
    // 共享的 supervisor 对象
    public static Supervisor supervisor;

    public Label getLbl_securityQuestion() {
        return lbl_securityQuestion;
    }

    public void setLbl_securityQuestion(Label lbl_securityQuestion) {
        this.lbl_securityQuestion = lbl_securityQuestion;
    }

    public TextField getTxt_securityAnswer() {
        return txt_securityAnswer;
    }

    public void setTxt_securityAnswer(TextField txt_securityAnswer) {
        this.txt_securityAnswer = txt_securityAnswer;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        NepsForgotPasswordAnswerController.primaryStage = primaryStage;
    }

    public static Supervisor getSupervisor() {
        return supervisor;
    }

    public static void setSupervisor(Supervisor supervisor) {
        NepsForgotPasswordAnswerController.supervisor = supervisor;
    }

    public void initialize() {
        // 初始化时显示密保问题
        if (supervisor != null) {
            lbl_securityQuestion.setText(supervisor.getSecurityQuestion(lbl_securityQuestion.getText()));
        }
    }

    public void submitAnswer() {
        String answer = txt_securityAnswer.getText();
        if (answer.equals(supervisor.getSecurityAnswer(txt_securityAnswer.getText()))) {
            // 将 supervisor 对象传递给 Reset 控制器
            NepsForgotPasswordResetController.supervisor = supervisor;
            //传递舞台
            NepsForgotPasswordResetController.primaryStage=primaryStage;
            // 显示重置密码界面
            JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordResetView.fxml", primaryStage, "忘记密码 - 重置密码");
        } else {
            JavafxUtil.showAlert(primaryStage, "答案错误", "密保问题答案错误", "请重新输入答案", "warn");
        }
    }

    public void back() {
        // 返回手机号输入界面
        JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordPhoneView.fxml", primaryStage, "忘记密码 - 输入手机号");
    }
}