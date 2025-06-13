package com.nep.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yanghq
 * 省市区域
 */

public class ProvinceCity  implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer provinceId;                              // 省区域编码
    private String provinceName;                             // 省区域名称
    private List<String> cityName = new ArrayList<String>(); // 省所包含的市名称

    public ProvinceCity() {
    }

    public ProvinceCity(Integer provinceId, String provinceName, List<String> cityName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.cityName = cityName;
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

    @Override
    public String toString() {
        return "ProvinceCity{" +
                "provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", cityName=" + cityName +
                '}';
    }
}
