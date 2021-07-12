package com.wolfking.jeesite.modules.md.entity;

import lombok.Getter;

/**
 * @author Ryan Lu
 * @version 1.0
 * 客户基本资料按需读取标志枚举
 * @date 2020/5/18 2:14 下午
 */
@Getter
public enum CustomerRequiredTagEnum {
    FINANCE(1, "财务", 1),
    SALE(2, "业务", 2)
    ;
    // 枚举值
    private Integer code;
    // 枚举描述
    private String desc;
    // 状态位
    private Integer tag;

    CustomerRequiredTagEnum(Integer code, String desc, Integer tag) {
        this.code = code;
        this.desc = desc;
        this.tag = tag;
    }

    public boolean hasTag(int tags) {
        return (tags & this.tag) == tag;
    }
}
