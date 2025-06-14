package com.nep.entity;

import java.io.Serializable;

public class AqiFeedback implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer afId;
    private String afName;
    private String proviceName;
    private String cityName;
    private String address;

    private String information;
    private String estimateGrade;
    private String dateFeedback; // 修改为 dateFeedback
    private String state;

    private String gmName;
    private String confirmDate;
    private Double so2;
    private Double co;
    private Double pm;
    private String confirmLevel;
    private String confirmExplain;

    public AqiFeedback() {
    }

    public AqiFeedback(Integer afId, String afName, String proviceName, String cityName, String address, String information, String estimateGrade, String dateFeedback, String state, String gmName, String confirmDate, Double so2, Double co, Double pm, String confirmLevel, String confirmExplain) {
        this.afId = afId;
        this.afName = afName;
        this.proviceName = proviceName;
        this.cityName = cityName;
        this.address = address;
        this.information = information;
        this.estimateGrade = estimateGrade;
        this.dateFeedback = dateFeedback; // 对应数据库的 date_feedback
        this.state = state;
        this.gmName = gmName;
        this.confirmDate = confirmDate;
        this.so2 = so2;
        this.co = co;
        this.pm = pm;
        this.confirmLevel = confirmLevel;
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
                ", information='" + information + '\'' +
                ", estimateGrade='" + estimateGrade + '\'' +
                ", dateFeedback='" + dateFeedback + '\'' + // 修改这里
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getEstimateGrade() {
        return estimateGrade;
    }

    public void setEstimateGrade(String estimateGrade) {
        this.estimateGrade = estimateGrade;
    }

    // 修改 getter/setter 名称
    public String getDateFeedback() {
        return dateFeedback;
    }

    public void setDateFeedback(String dateFeedback) {
        this.dateFeedback = dateFeedback;
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
}