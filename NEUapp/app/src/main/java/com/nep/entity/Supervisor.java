package com.nep.entity;

import java.io.Serializable;

/**
 * @author ljyl
 * 公众监督员信息
 */
public class Supervisor extends Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sex ;	//公众监督员性别  男/女
    private String securityQuestion;
    private String securityAnswer;

    public Supervisor(String loginCode, String password, String realName, String sex,String securityQuestion,String securityAnswer) {
        super(loginCode, password, realName);
        this.sex = sex;
        this.securityQuestion=securityQuestion;
        this.securityAnswer=securityAnswer;
    }

    public Supervisor() {

    }

    @Override
    public String toString() {
        return "Supervisor{" +
                "sex='" + sex + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                '}';
    }

    // 修正后的 getter 方法，不应有参数
    public String getSecurityQuestion() { //
        return securityQuestion; //
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    // 修正后的 getter 方法，不应有参数
    public String getSecurityAnswer() { //
        return securityAnswer; //
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
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


//构造方法及setter/getter方法......
}
