package com.wolfking.jeesite.modules.api.entity.md;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取网点收货地址信息
 */
public class AppGetEngineerConsigneeAddressResponse {

    /**
     * 是否存在收货地址：1 - 存在收、0 - 不存在
     */
    @Getter
    @Setter
    private Integer hasConsigneeAddress = 0;

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
     * 区域ID
     */
    @Getter
    @Setter
    private Long areaId = 0L;

    /**
     * 区域名称（全称：广东省深圳市龙华区）
     */
    @Getter
    @Setter
    private String areaName = "";

    /**
     * 详细地址（省市区街道门牌号）
     */
    @Getter
    @Setter
    private String address = "";


}
