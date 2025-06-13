package com.nep.entity;

import java.io.Serializable;

/**
 * @author Yanghq
 * 网格员信息
 */
public class GridMember extends Operator implements Serializable {
    private static final long serialVersionUID = 1L;
    private String gmTel;    //网格员联系电话
    private String state;    //网格员状态: 工作中,休假中

    public GridMember(String loginCode, String password, String realName, String gmTel, String state) {
        super(loginCode, password, realName);
        this.gmTel = gmTel;
        this.state = state;
    }

    public GridMember() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getGmTel() {
        return gmTel;
    }

    public void setGmTel(String gmTel) {
        this.gmTel = gmTel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

 @Override
 public String toString() {
  return "GridMember{" +
          "gmTel='" + gmTel + '\'' +
          ", state='" + state + '\'' +
          '}';
 }
}
