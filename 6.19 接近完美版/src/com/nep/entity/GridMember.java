package com.nep.entity;

import java.io.Serializable;

/**
 * @author ljyl
 * 网格员信息
 */
public class GridMember extends Operator implements Serializable {

    private static final long serialVersionUID = 1L;
    private String gmTel;	//网格员联系电话

    public GridMember(String loginCode, String password, String realName) {
        super(loginCode, password, realName);
    }

    public GridMember() {

    }

    @Override
    public String toString() {
        return "GridMember{" +
                "gmTel='" + gmTel + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public void setGmTel(String gmTel) {
        this.gmTel = gmTel;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGmTel() {
        return gmTel;
    }

    public String getState() {
        return state;
    }

    private String state;	//网格员状态: 工作中,休假中
    //构造方法及setter/getter方法......
}
