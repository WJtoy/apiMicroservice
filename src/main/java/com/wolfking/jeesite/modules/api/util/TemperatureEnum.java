package com.wolfking.jeesite.modules.api.util;

/**
 * @Auther wj
 * @Date 2021/1/15 11:34
 */
public enum TemperatureEnum {
    normal(10,"体温正常"),
    abnormal(20,"异常"),
    abnormalHealth(21,"体温正常，健康申报异常"),
    abnormalTemperature(22,"体温异常，健康申报正常"),
    abnormalHealthAndTemperature(23,"体温异常，健康申报异常");

    public String healthStatus;
    public int value;
    // 构造方法
    private TemperatureEnum(int value, String healthStatus) {
        this.value = value;
        this.healthStatus = healthStatus;
    }
}
