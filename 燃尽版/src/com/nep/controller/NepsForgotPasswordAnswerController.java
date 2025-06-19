package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl; // 导入 SupervisorServiceImpl
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
    // 共享的 supervisor 对象 (从 NepsForgotPasswordViewController 传递过来)
    public static Supervisor supervisor;

    // 增加 SupervisorService 实例，用于从数据库获取密保问题和答案
    private SupervisorService supervisorService = new SupervisorServiceImpl();

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

    /**
     * 初始化方法，显示用户的密保问题。
     * 密保问题应该从数据库中加载的 Supervisor 对象中获取。
     */
    public void initialize() {
        if (supervisor != null) {
            // 从传递过来的 supervisor 对象中获取安全问题并显示
            // 假设 Supervisor 实体类中有一个方法 getSecurityQuestion() 来直接获取问题字符串
            lbl_securityQuestion.setText(supervisor.getSecurityQuestion());
        } else {
            JavafxUtil.showAlert(primaryStage, "错误", "用户信息缺失", "无法获取密保问题，请返回重试。", "warn");
            // 考虑在此处跳转回 NepsForgotPasswordViewController
            back();
        }
    }

    /**
     * 提交密保问题答案，验证并跳转到重置密码界面。
     */
    public void submitAnswer() {

        String answer = txt_securityAnswer.getText();// 获取用户输入的答案并去除空格
        if (answer.isEmpty()) {
            JavafxUtil.showAlert(primaryStage, "输入错误", "答案不能为空", "请输入您的密保问题答案。", "warn");
            return;
        }

        if (supervisor != null) {
            // 从传递过来的 supervisor 对象中获取正确的安全答案进行比对
            // 假设 Supervisor 实体类中有一个方法 getSecurityAnswer() 来直接获取答案字符串
            if (answer.equals(supervisor.getSecurityAnswer())) {
                // 将 supervisor 对象传递给 Reset 控制器
                NepsForgotPasswordResetController.supervisor = supervisor;
                //传递舞台
                NepsForgotPasswordResetController.primaryStage = primaryStage;
                // 显示重置密码界面
                JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordResetView.fxml", primaryStage, "忘记密码 - 重置密码");
            } else {
                JavafxUtil.showAlert(primaryStage, "答案错误", "密保问题答案错误", "请重新输入答案", "warn");
            }
        } else {
            JavafxUtil.showAlert(primaryStage, "错误", "用户信息缺失", "无法验证密保问题，请返回重试。", "warn");
            back(); // 返回上一步
        }
    }

    /**
     * 返回上一个界面（手机号输入界面）。
     */
    public void back() {
        // 返回手机号输入界面
        JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordView.fxml", primaryStage, "忘记密码 - 找回密码");
    }
}