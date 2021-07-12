package com.wolfking.jeesite.modules.api.entity.md;

import lombok.Getter;
import lombok.Setter;

/**
 * 获取师傅个人地址和网点地址
 */
public class AppGetEngineerAndServicePointAddressResponse {

    @Setter
    @Getter
    private EngineerAddress engineer = new EngineerAddress();

    @Setter
    @Getter
    private ServicePointAddress servicePoint = new ServicePointAddress();

    public static class EngineerAddress {
        /**
         * 师傅姓名
         */
        @Getter
        @Setter
        private String name = "";
        /**
         * 手机号
         */
        @Getter
        @Setter
        private String contactInfo = "";
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

    public static class ServicePointAddress {
        /**
         * 网点主账号姓名
         */
        @Getter
        @Setter
        private String name = "";
        /**
         * 手机号
         */
        @Getter
        @Setter
        private String contactInfo = "";
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

}
