package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Supervisor;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NepsRegisterViewController {
    public static Stage PrimaryStage;
    @FXML
    private TextField txt_id;
    @FXML
    private PasswordField txt_password;
    @FXML
    private PasswordField txt_repassword;
    @FXML
    private TextField txt_realName;
    @FXML
    private RadioButton txt_sex;

    public TextField getTxt_id() {
        return txt_id;
    }
    public void setTxt_id(TextField txt_id) {
        this.txt_id = txt_id;
    }
    public PasswordField getTxt_password() {
        return txt_password;
    }
    public void setTxt_password(PasswordField txt_password) {
        this.txt_password = txt_password;
    }
    public PasswordField getTxt_repassword() {
        return txt_repassword;
    }
    public void setTxt_repassword(PasswordField txt_repassword) {
        this.txt_repassword = txt_repassword;
    }
    public TextField getTxt_realName() {
        return txt_realName;
    }
    public void setTxt_realName(TextField txt_realName) {
        this.txt_realName = txt_realName;
    }

    public RadioButton getTxt_sex() {
        return txt_sex;
    }

    public void setTxt_sex(RadioButton txt_sex) {
        this.txt_sex = txt_sex;
    }


    private SupervisorService supervisorService = new SupervisorServiceImpl();
    public void register(){
        //进行表单的验证
        //密码输入是否一致
        //用户名是否为空
        if (txt_id.getText().equals("")){
            JavafxUtil.showAlert(PrimaryStage, "用户名为空", "用户名为空", "请输入用户名", "warn");
            return;
        }
        if(!txt_password.getText().equals(txt_repassword.getText())){
            JavafxUtil.showAlert(PrimaryStage, "注册失败", "两次密码不一致", "请输入确认密码", "warn");
            txt_repassword.setText("");
            return;
        }

        Supervisor supervisor = new Supervisor();
        supervisor.setLoginCode(txt_id.getText());
        supervisor.setPassword(txt_password.getText());
        supervisor.setRealName(txt_sex.getText());
        supervisor.setSex(txt_sex.getText());

        //调用注册方法
        boolean register=supervisorService.register(supervisor);

        if(register){
            JavafxUtil.showAlert(PrimaryStage, "注册成功！", txt_id.getText()+"账号注册成功，请保存", "可以进行登录", "info");
        }
        else {
            JavafxUtil.showAlert(PrimaryStage, "注册失败！", "该手机号已注册", "请重新输入手机号", "warn");
            txt_id.setText("");
            return;
        }
        //注册成功页面跳转

        JavafxUtil.showStage(NepsMain.class, "view/NepsLoginView.fxml", PrimaryStage, "东软环保公众监督平台-公众监督员端");
    }
    public void back(){
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginView.fxml", PrimaryStage,"东软环保公众监督平台-公众监督员端");
    }
}