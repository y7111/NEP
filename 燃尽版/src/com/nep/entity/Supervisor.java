package com.nep.entity;

import java.io.Serializable;

/**
 * @author ljyl
 * 公众监督员信息
 */
public class Supervisor extends Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sex ;   //公众监督员性别  男/女
    private String securityQuestion; // 新增：密保问题
    private String securityAnswer;   // 新增：密保问题答案

    public Supervisor(String loginCode, String password, String realName, String sex, String securityQuestion, String securityAnswer) {
        super(loginCode, password, realName);
        this.sex = sex;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public Supervisor() {
        // 无参构造函数也要保留
    }

    @Override
    public String toString() {
        return "Supervisor{" +
                "loginCode='" + getLoginCode() + '\'' +
                ", realName='" + getRealName() + '\'' +
                ", sex='" + sex + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    // 新增：securityQuestion 的 getter 和 setter
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    // 新增：securityAnswer 的 getter 和 setter
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}