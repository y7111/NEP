package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NepsLoginViewController {
    @FXML
    private TextField txt_id;
    @FXML
    private PasswordField txt_password;

    public static Stage primaryStage;
    private SupervisorService supervisorService = new SupervisorServiceImpl();

    public void login() {
        NepsSelectAqiViewController.primaryStage = primaryStage;
        String loginCode = txt_id.getText().trim();
        String password = txt_password.getText().trim();

        if (loginCode.isEmpty() || password.isEmpty()) {
            JavafxUtil.showAlert(primaryStage, "输入错误", "账号或密码不能为空", "请输入您的账号和密码。", "warn");
            return;
        }

        Supervisor supervisor = supervisorService.login(loginCode, password);

        if (supervisor != null) {
            // 登录成功，跳转到主界面
            JavafxUtil.showAlert(primaryStage, "登录成功", "欢迎", "您已成功登录系统！", "info");
            // 这里可以根据需要跳转到监督员主界面
            // 假设 NepsSelectAqiViewController 是登录后的主界面
            // 注意: NepsSelectAqiViewController.supervisor 已经在 SupervisorServiceImpl.login 方法中设置
            JavafxUtil.showStage(NepsMain.class, "view/NepsSelectAqiView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
        } else {
            JavafxUtil.showAlert(primaryStage, "登录失败", "账号或密码错误", "请检查您的输入。", "warn");
        }
    }

    public void register() {
        // 跳转到注册界面
        NepsRegisterViewController.primaryStage = primaryStage;
        JavafxUtil.showStage(NepsMain.class, "view/NepsRegisterView.fxml", primaryStage, "东软环保公众监督平台-公众监督员注册");
    }

    public void forgotPassword() {
        // 跳转到忘记密码界面
        NepsForgotPasswordViewController.primaryStage = primaryStage; // 传递舞台
        JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordView.fxml", primaryStage, "忘记密码 - 找回密码");
    }
}