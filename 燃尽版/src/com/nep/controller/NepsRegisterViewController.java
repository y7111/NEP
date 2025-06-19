package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import com.nep.util.SHA512Util;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;

public class NepsRegisterViewController {

    // 确保这些 fx:id 与 FXML 文件中的 fx:id 完全匹配
    @FXML
    private TextField txt_regLoginCode;
    @FXML
    private PasswordField txt_regPassword;
    @FXML
    private PasswordField txt_regRePassword; // 对应 FXML 中的 txt_regRePassword
    @FXML
    private TextField txt_regRealName;

    @FXML
    private RadioButton rbMale;             // 对应 FXML 中的 rbMale
    @FXML
    private RadioButton rbFemale;           // 对应 FXML 中的 rbFemale
    @FXML
    private ToggleGroup sexToggleGroup;     // 对应 FXML 中的 sexToggleGroup

    @FXML
    private TextField txt_securityQuestion;
    @FXML
    private TextField txt_securityAnswer;

    public static Stage primaryStage;
    private SupervisorService supervisorService = new SupervisorServiceImpl();

    public void register() {
        // 检查这里所有的 getText() 调用是否发生在 null 对象上
        String loginCode = txt_regLoginCode.getText().trim();
        String password = txt_regPassword.getText().trim();
        String rePassword = txt_regRePassword.getText().trim(); // 如果第42行是这里，那很可能是 txt_regRePassword 是 null
        String realName = txt_regRealName.getText().trim();

        String sex = "";
        // 检查 sexToggleGroup 是否为 null
        if (sexToggleGroup != null && sexToggleGroup.getSelectedToggle() != null) {
            sex = ((RadioButton) sexToggleGroup.getSelectedToggle()).getText();
        } else {
            // 如果 sexToggleGroup 为 null 或未选择，弹出警告
            JavafxUtil.showAlert(primaryStage, "输入错误", "性别未选择", "请选择您的性别。", "warn");
            return;
        }

        String securityQuestion = txt_securityQuestion.getText().trim();
        String securityAnswer = txt_securityAnswer.getText().trim();

        // --- 输入校验 ---
        if (loginCode.isEmpty() || password.isEmpty() || rePassword.isEmpty() || realName.isEmpty()
                || sex.isEmpty() || securityQuestion.isEmpty() || securityAnswer.isEmpty()) {
            JavafxUtil.showAlert(primaryStage, "输入错误", "注册信息不完整", "所有字段都不能为空，请填写完整。", "warn");
            return;
        }

        // 密码一致性校验
        if (!password.equals(rePassword)) {
            JavafxUtil.showAlert(primaryStage, "注册失败", "两次输入密码不一致", "请重新输入确认密码", "warn");
            txt_regRePassword.setText("");
            return;
        }

        String encryptedPassword = SHA512Util.encrypt(password, loginCode);

        Supervisor newSupervisor = new Supervisor(loginCode, encryptedPassword, realName, sex, securityQuestion, securityAnswer);

        if (supervisorService.register(newSupervisor)) {
            JavafxUtil.showAlert(primaryStage, "注册成功", "恭喜您", "账号注册成功，请返回登录！", "info");
            JavafxUtil.showStage(NepsMain.class, "view/NepsLoginView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
        } else {
            JavafxUtil.showAlert(primaryStage, "注册失败", "账号已存在或注册失败", "请更换登录账号或稍后再试。", "warn");
            txt_regLoginCode.setText("");
        }
    }

    public void back() {
        JavafxUtil.showStage(NepsMain.class, "view/NepsLoginView.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
    }
}