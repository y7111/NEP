package com.nep.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProvinceCity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer provinceId;
    private String provinceName;
    private List<String> cityName = new ArrayList<String>();

    public ProvinceCity() {
    }

    public ProvinceCity(Integer provinceId, String provinceName, List<String> cityName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.cityName = cityName;
    }

    // 新增一个构造函数，用于从数据库中读取字符串形式的城市名
    public ProvinceCity(Integer provinceId, String provinceName, String cityNamesString) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        if (cityNamesString != null && !cityNamesString.isEmpty()) {
            this.cityName = Arrays.asList(cityNamesString.split(","));
        } else {
            this.cityName = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "ProvinceCity{" +
                "provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityName=" + cityName +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<String> getCityName() {
        return cityName;
    }

    public void setCityName(List<String> cityName) {
        this.cityName = cityName;
    }

    // 新增一个方法，用于将 List<String> 转换为逗号分隔的字符串
    public String getCityNamesAsString() {
        if (cityName == null || cityName.isEmpty()) {
            return "";
        }
        return cityName.stream().collect(Collectors.joining(","));
    }
}