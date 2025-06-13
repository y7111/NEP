package com.nep.entity;

/**
 * @author Yanghq
 * Aqi反馈信息
 */
public class AqiFeedback {
    private static final long serialVersionUID = 1L;
    private Integer afId;          // 反馈信息编号
    private String afName;         // 公众监督员姓名
    private String proviceName;    // 省网格区域
    private String cityName;       // 市网格区域
    private String address;        // 具体地址
    private String infomation;     // 详细反馈信息
    private String estimateGrade;  // 预估等级
    private String date;           // 反馈日期
    private String state;          // 反馈状态: 未指派,已指派,已实测
    private String gmName;         // 指派网格员
    private String confirmDate;    // 实测日期
    private Double so2;            // 实测二氧化硫浓度
    private Double co;             // 实测一氧化碳浓度
    private Double pm;             // 实测PM2.5浓度
    private String confirmLevel;   // 实测AQI等级
    private String confirmExplain; // 实测AQI等级描述

    public AqiFeedback(Integer afId, String afName, String proviceName, String cityName, String address, String infomation, String estimateGrade, String date, String state, String gmName, String confirmDate, Double so2, Double co, Double pm, String confirmLevel, String confirmExplain) {
        this.afId = afId;
        this.afName = afName;
        this.proviceName = proviceName;
        this.cityName = cityName;
        this.address = address;
        this.infomation = infomation;
        this.estimateGrade = estimateGrade;
        this.date = date;
        this.state = state;
        this.gmName = gmName;
        this.confirmDate = confirmDate;
        this.so2 = so2;
        this.co = co;
        this.pm = pm;
        this.confirmLevel = confirmLevel;
        this.confirmExplain = confirmExplain;
    }

    public AqiFeedback() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAfId() {
        return afId;
    }

    public void setAfId(Integer afId) {
        this.afId = afId;
    }

    public String getAfName() {
        return afName;
    }

    public void setAfName(String afName) {
        this.afName = afName;
    }

    public String getProviceName() {
        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public String getEstimateGrade() {
        return estimateGrade;
    }

    public void setEstimateGrade(String estimateGrade) {
        this.estimateGrade = estimateGrade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGmName() {
        return gmName;
    }

    public void setGmName(String gmName) {
        this.gmName = gmName;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public Double getSo2() {
        return so2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public Double getCo() {
        return co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Double getPm() {
        return pm;
    }

    public void setPm(Double pm) {
        this.pm = pm;
    }

    public String getConfirmLevel() {
        return confirmLevel;
    }

    public void setConfirmLevel(String confirmLevel) {
        this.confirmLevel = confirmLevel;
    }

    public String getConfirmExplain() {
        return confirmExplain;
    }

    public void setConfirmExplain(String confirmExplain) {
        this.confirmExplain = confirmExplain;
    }

    @Override
    public String toString() {
        return "AqiFeedback{" +
                "afId=" + afId +
                ", afName='" + afName + '\'' +
                ", proviceName='" + proviceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", address='" + address + '\'' +
                ", infomation='" + infomation + '\'' +
                ", estimateGrade='" + estimateGrade + '\'' +
                ", date='" + date + '\'' +
                ", state='" + state + '\'' +
                ", gmName='" + gmName + '\'' +
                ", confirmDate='" + confirmDate + '\'' +
                ", so2=" + so2 +
                ", co=" + co +
                ", pm=" + pm +
                ", confirmLevel='" + confirmLevel + '\'' +
                ", confirmExplain='" + confirmExplain + '\'' +
                '}';
    }
}
