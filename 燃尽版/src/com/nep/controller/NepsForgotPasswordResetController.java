package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import com.nep.util.SHA512Util; // 导入SHA512Util
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class NepsForgotPasswordResetController {
    @FXML
    private PasswordField txt_newPassword;
    @FXML
    private PasswordField txt_confirmPassword;

    // 主舞台：需要确保在跳转到此控制器时，这个静态变量被赋值
    public static Stage primaryStage;
    // 共享的 supervisor 对象：从 NepsForgotPasswordAnswerController 传递过来，确保它是完整的用户数据
    public static Supervisor supervisor; // 这个对象必须包含 loginCode, realName, sex, securityQuestion, securityAnswer 等所有字段

    private SupervisorService supervisorService = new SupervisorServiceImpl();

    /**
     * 重置密码。
     * 会对新密码进行加密后存入数据库。
     */
    public void resetPassword() {
        String newPassword = txt_newPassword.getText().trim(); // 使用trim()移除空白
        String confirmPassword = txt_confirmPassword.getText().trim();

        // 1. 输入校验：检查密码是否为空
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JavafxUtil.showAlert(primaryStage, "输入错误", "密码不能为空", "请输入您的新密码和确认密码。", "warn");
            return;
        }

        // 2. 密码一致性校验
        if (!newPassword.equals(confirmPassword)) {
            JavafxUtil.showAlert(primaryStage, "密码不一致", "两次输入的新密码不一致", "请重新输入。", "warn");
            txt_confirmPassword.setText(""); // 清空确认密码字段
            return; // 结束方法执行
        }

        // 3. 核心逻辑：确保 supervisor 对象及其关键信息不为空
        if (supervisor != null && supervisor.getLoginCode() != null && !supervisor.getLoginCode().isEmpty()) {
            // 对新密码进行加密
            String encryptedNewPassword = SHA512Util.encrypt(newPassword, supervisor.getLoginCode());

            // 4. 更新 supervisor 对象的密码字段 (本地内存中)
            // 因为你Service层使用的是 updateSupervisor(Supervisor supervisor)
            // 所以需要将这个supervisor对象中的密码更新为新的加密密码，再传给Service层
            supervisor.setPassword(encryptedNewPassword);

            // 5. 调用 service 更新用户信息到数据库
            // 使用你Service中已有的 updateSupervisor 方法
            if (supervisorService.updateSupervisor(supervisor)) { // 调用 updateSupervisor
                JavafxUtil.showAlert(primaryStage, "重置成功", "密码重置成功", "可以使用新密码登录！", "info");
                // 成功后跳转到登录界面
                JavafxUtil.showStage(NepsMain.class, "view/NepsLoginView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
            } else {
                // 数据库更新操作失败的情况
                System.out.println("数据库更新密码操作失败。请检查SupervisorServiceImpl的updateSupervisor方法及DBUtil连接。"); // 调试输出
                JavafxUtil.showAlert(primaryStage, "重置失败", "密码重置失败", "数据库操作失败，请稍后再试。", "error");
            }
        } else {
            // supervisor 对象或其 loginCode 为 null/空，无法进行密码重置
            JavafxUtil.showAlert(primaryStage, "错误", "用户信息缺失或不完整", "无法重置密码，请返回上一步重新尝试。", "warn");
            back(); // 返回上一步
        }
    }

    /**
     * 返回上一个界面（密保问题回答界面）。
     */
    public void back() {
        // 返回密保问题回答界面
        JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordAnswerView.fxml", primaryStage, "忘记密码 - 回答密保问题");
    }
}