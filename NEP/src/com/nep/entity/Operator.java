package com.nep.entity;

import java.io.Serializable;

/**
 * @author w
 * 操作员（管理员，网格员，公众监督员父类）
 */
public class Operator implements Serializable {
    private static final long serialVersionUID = 1L;
    private String loginCode;     //登录账号
    private String password ;     //登录密码
    private String realName;      //真实姓名

    public Operator() {
    }

    public Operator(String loginCode, String password, String realName) {
        this.loginCode = loginCode;
        this.password = password;
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "loginCode='" + loginCode + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}