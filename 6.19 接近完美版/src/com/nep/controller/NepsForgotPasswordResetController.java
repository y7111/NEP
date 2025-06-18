package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class NepsForgotPasswordResetController {
    @FXML
    private PasswordField txt_newPassword;
    @FXML
    private PasswordField txt_confirmPassword;

    // 主舞台
    public static Stage primaryStage;
    // 共享的 supervisor 对象
    public static Supervisor supervisor;
    private SupervisorService supervisorService = new SupervisorServiceImpl();

    public void resetPassword() {
        String newPassword = txt_newPassword.getText();
        String confirmPassword = txt_confirmPassword.getText();
        if (newPassword.equals(confirmPassword)) {
            supervisor.setPassword(newPassword);
            supervisorService.register(supervisor); // 更新用户信息
            JavafxUtil.showAlert(primaryStage, "重置成功", "密码重置成功", "可以使用新密码登录", "info");
            // 跳转到登录界面
            JavafxUtil.showStage(NepsMain.class, "view/NepsLoginViewa.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
        } else {
            JavafxUtil.showAlert(primaryStage, "密码不一致", "两次输入的新密码不一致", "请重新输入", "warn");
        }
    }

    public void back() {
        // 返回密保问题界面
        JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordAnswerView.fxml", primaryStage, "忘记密码 - 回答密保问题");
    }
}