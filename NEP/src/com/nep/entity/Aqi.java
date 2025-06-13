package com.nep.entity;

import java.io.Serializable;

/**
 @author Yanghq
 AQI标准指数
 */

public class Aqi implements Serializable {
    //aqi 指数级别
    private String level;
    //aqi 指数描述
    private String explain;
    //对健康的影响
    private String impact;

    public Aqi() {
    }

    public Aqi(String level, String explain, String impact) {
        this.level = level;
        this.explain = explain;
        this.impact = impact;
    }

    @Override
    public String toString() {
        return "Aqi{" +
                "level='" + level + '\'' +
                ", explain='" + explain + '\'' +
                ", impact='" + impact + '\'' +
                '}';
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }
}
