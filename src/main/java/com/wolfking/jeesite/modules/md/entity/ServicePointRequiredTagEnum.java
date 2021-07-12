package com.wolfking.jeesite.modules.md.entity;

import lombok.Getter;

/**
 * @author Ryan Lu
 * @version 1.0
 * 客户基本资料按需读取标志枚举
 * @date 2020/5/18 2:14 下午
 */
@Getter
public enum ServicePointRequiredTagEnum {
    FINANCE(1, "财务", 1),
    FINANCE_BANK_INFO(2,"付款信息",2),
    CATEGORY_NAMES(2, "品类名称", 4),
    PRIMAY(3, "主帐号信息", 8),
    AREA(4, "主帐号信息", 16)
    ;
    // 枚举值
    private Integer code;
    // 枚举描述
    private String desc;
    // 状态位
    private Integer tag;

    ServicePointRequiredTagEnum(Integer code, String desc, Integer tag) {
        this.code = code;
        this.desc = desc;
        this.tag = tag;
    }

    public boolean hasTag(int tags) {
        return (tags & this.tag) == tag;
    }
}
