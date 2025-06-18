package com.nep.controller;

import com.nep.util.JavafxUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink; // 确保导入 Hyperlink 类
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.nep.service.SupervisorService; // 导入SupervisorService
import com.nep.service.impl.SupervisorServiceImpl; // 导入SupervisorServiceImpl
import com.nep.util.SHA512Util; // 导入SHA512Util


public class NepsLoginViewController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink forgotPasswordLink; // 添加这个声明，对应 FXML 中的 fx:id

    private SupervisorService supervisorService; // 声明SupervisorService

    public NepsLoginViewController() {
        this.supervisorService = new SupervisorServiceImpl(); // 实例化SupervisorService
    }

    @FXML
    public void initialize() {
        // 现有的初始化逻辑 (如果存在)
        // 例如：usernameField.setText("default_username");
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JavafxUtil.showAlert(Alert.AlertType.WARNING, "警告", "用户名和密码不能为空！");
            return;
        }

        // 仅对Supervisor进行登录验证
        String hashedPassword = SHA512Util.hashPassword(password);
        if (supervisorService.login(username, hashedPassword)) {
            JavafxUtil.showAlert(Alert.AlertType.INFORMATION, "登录成功", "欢迎，" + username + "！");
            // 登录成功后跳转到主界面
            JavafxUtil.changeScene(event, "/com/nep/view/NepmMainView.fxml"); // 假设这是Supervisor的主界面
        } else {
            JavafxUtil.showAlert(Alert.AlertType.ERROR, "登录失败", "用户名或密码不正确！");
        }
    }

    /**
     * 处理“忘记密码”链接点击事件。
     * 导航到 NepsForgotPasswordView.fxml 界面。
     * @param event ActionEvent
     */
    @FXML
    private void handleForgotPasswordLink(ActionEvent event) {
        JavafxUtil.changeScene(event, "/com/nep/view/NepsForgotPasswordView.fxml");
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        // 假设这里是跳转到注册界面
        JavafxUtil.changeScene(event, "/com/nep/view/NepsRegisterView.fxml");
    }
}