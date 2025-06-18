package com.nep.controller;

import com.nep.NepsMain;
import com.nep.entity.Aqi;
import com.nep.entity.Supervisor;
import com.nep.entity.securityQuestion;
import com.nep.io.FileIO;
import com.nep.service.SupervisorService;
import com.nep.service.impl.SupervisorServiceImpl;
import com.nep.util.JavafxUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class NepsRegisterViewController implements Initializable {
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

    @FXML
    private ComboBox<String> txt_securityQuestion;
    @FXML
    private TextField txt_securityAnswer;

    public ComboBox<String> getTxt_securityQuestion() {
        return txt_securityQuestion;
    }

    public void setTxt_securityQuestion(ComboBox<String> txt_securityQuestion) {
        this.txt_securityQuestion = txt_securityQuestion;
    }

    //主舞台
    public static Stage primaryStage;
    //多态
    private SupervisorService supervisorService = new SupervisorServiceImpl();

    public TextField getTxt_id() {
        return txt_id;
    }

    public void setTxt_id(TextField txt_id) {
        this.txt_id = txt_id;
    }

    public TextField getTxt_securityAnswer() {
        return txt_securityAnswer;
    }

    public void setTxt_securityAnswer(TextField txt_securityAnswer) {
        this.txt_securityAnswer = txt_securityAnswer;
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<securityQuestion> elist=(List<securityQuestion>) FileIO.readObject("securityQuestion.txt");
        System.out.println(elist);

        for(securityQuestion securityQuestion:elist){
            txt_securityQuestion.getItems().add(securityQuestion.getQuestion());
        }
        txt_securityQuestion.setValue("选择密保问题");
    }
    public  void register(){
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
        supervisor.setSex(txt_sex.getText());
        supervisor.setSecurityQuestion(txt_securityQuestion.getValue());
        supervisor.setSecurityAnswer(txt_securityAnswer.getText());


        boolean flag = supervisorService.register(supervisor);
        if(flag){
            JavafxUtil.showAlert(primaryStage, "注册成功", txt_id.getText()+" 账号注册成功!","可以进行用户登录!" ,"info");
        }else{
            JavafxUtil.showAlert(primaryStage, "注册失败", "手机号已被注册", "请重新输入注册手机号码","warn");
            txt_id.setText("");
            return;
    }
        //跳转到登录界面进行登录
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginViewa.fxml", primaryStage,"东软环保公众监督平台-公众监督员端");
}
    public void back(){
        JavafxUtil.showStage(NepsMain.class,"view/NepsLoginViewa.fxml", primaryStage,"东软环保公众监督平台-公众监督员端");
    }


}