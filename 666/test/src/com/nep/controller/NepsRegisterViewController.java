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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;

public class NepsRegisterViewController {
    @FXML
    private TextField txt_id;
    @FXML
    private PasswordField txt_password;
    @FXML
    private PasswordField txt_repassword;
    @FXML
    private TextField txt_realName;

    @FXML
    private RadioButton txt_sex; // 根据你原有的FML文件，可能是这个名字

    public static Stage primaryStage;
    private SupervisorService supervisorService = new SupervisorServiceImpl();

    // ... 省略 getter/setter 方法 ...
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

    public void register(){
        if(txt_id.getText().equals("")){
            JavafxUtil.showAlert(primaryStage,"用户名为空","用户名为空","请输入用户名","warn");
            return;
        }
        if(!txt_password.getText().equals(txt_repassword.getText())){
            JavafxUtil.showAlert(primaryStage, "注册失败", "两次输入密码不一致", "请重新输入确认密码","warn");
            txt_repassword.setText("");
            return;
        }

        Supervisor supervisor=new Supervisor();
        supervisor.setLoginCode(txt_id.getText());
        supervisor.setPassword(txt_password.getText());
        supervisor.setRealName(txt_realName.getText());

        String selectedSex = txt_sex.getText();
        if (selectedSex == null || selectedSex.isEmpty() || selectedSex.equals("RadioButton")) {
            JavafxUtil.showAlert(primaryStage, "注册失败", "性别未选择或无效", "请选择您的性别","warn");
            return;
        }

        supervisor.setSex(selectedSex);

        boolean flag = supervisorService.register(supervisor);
        if(flag){
            JavafxUtil.showAlert(primaryStage, "注册成功", txt_id.getText()+" 账号注册成功!","可以进行用户登录!" ,"info");
        }else{
            JavafxUtil.showAlert(primaryStage, "注册失败", "账号已被注册", "请重新输入注册账号","warn");
            txt_id.setText("");
            return;
        }
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginViewa.fxml", primaryStage,"东软环保公众监督平台-公众监督员端");
    }

    public void back(){
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginViewa.fxml", primaryStage,"东软环保公众监督平台-公众监督员端");
    }
}