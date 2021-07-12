package com.wolfking.jeesite.modules.api.util;

/**
 * @Auther wj
 * @Date 2021/1/18 10:41
 */
public enum HealthOptionEnum {
    healthNormal(10,"身心健康，满足安全健康上门条件"),
    healthAbnormal(20,"身体不适，存在发热、头晕、胸闷、呼吸困难、乏力、恶心呕吐、腹泻等症状或携带不良情绪。");
    public String label;
    public int value;
    // 构造方法
    private HealthOptionEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

}
