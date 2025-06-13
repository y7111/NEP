package com.nep.dto;

public class AqiLimitDto {
    private int intlevel;		//AQI等级数字形式
    private String level;		//AQI等级
    private String explain;		//AQI等级描述
    private String color;		//AQI等级对应颜色

    public AqiLimitDto(int intlevel, String level, String explain, String color) {
        this.intlevel = intlevel;
        this.level = level;
        this.explain = explain;
        this.color = color;
    }

    public AqiLimitDto() {
    }

    public int getIntlevel() {
        return intlevel;
    }

    public void setIntlevel(int intlevel) {
        this.intlevel = intlevel;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "AqilimitDto{" +
                "intlevel=" + intlevel +
                ", level='" + level + '\'' +
                ", explain='" + explain + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
