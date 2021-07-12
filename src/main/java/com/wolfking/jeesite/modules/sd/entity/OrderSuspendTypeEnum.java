package com.wolfking.jeesite.modules.sd.entity;

/**
 * 是否可见标记枚举
 */
public enum OrderSuspendTypeEnum {

    NONE(0, "无"),
    VALIDATE(10, "鉴定");

    private final int value;
    private final String label;

    OrderSuspendTypeEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
