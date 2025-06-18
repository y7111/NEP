package com.nep.controller;

import com.nep.NepsMain;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NepsForgotPasswordViewController {
    @FXML
    private TextField txt_id;

    // 主舞台
    public static Stage primaryStage;
    private SupervisorService supervisorService = new SupervisorServiceImpl();
    private Supervisor supervisor;

    public void submitPhone() {
        String phone = txt_id.getText();
        supervisor = supervisorService.getSupervisorByLoginCode(phone);
        if (supervisor != null) {
            // 将找到的 supervisor 对象传递给 Answer 控制器
            NepsForgotPasswordAnswerController.supervisor = supervisor;
            //传递舞台
            NepsForgotPasswordAnswerController.primaryStage=primaryStage;
            // 显示密保问题界面
            JavafxUtil.showStage(NepsMain.class, "view/NepsForgotPasswordAnswerView.fxml", primaryStage, "忘记密码 - 回答密保问题");
        } else {
            JavafxUtil.showAlert(primaryStage, "未找到用户", "未找到该手机号对应的用户", "请检查手机号是否正确", "warn");
        }
    }

    public void back() {
        // 返回登录界面
        JavafxUtil.showStage(NepsMain.class, "view/NepsLoginViewa.fxml", primaryStage, "东软环保公众监督平台-公众监督员端");
    }
}