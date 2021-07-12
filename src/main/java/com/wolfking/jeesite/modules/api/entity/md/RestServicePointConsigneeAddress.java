package com.wolfking.jeesite.modules.api.entity.md;

import lombok.Getter;
import lombok.Setter;

/**
 * 网点收货地址信息
 */
public class RestServicePointConsigneeAddress {

    /**
     * 联系人
     */
    @Getter
    @Setter
    private String userName = "";

    /**
     * 手机号
     */
    @Getter
    @Setter
    private String userPhone = "";

    /**
     * 区域名称（全称，如广东省深圳市龙华区）
     */
    @Getter
    @Setter
    private String areaName = "";

    /**
     * 详细地址（街道门牌号）
     */
    @Getter
    @Setter
    private String address = "";
}
