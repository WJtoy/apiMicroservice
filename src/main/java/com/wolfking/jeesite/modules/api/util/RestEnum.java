package com.wolfking.jeesite.modules.api.util;

public class RestEnum {

    /**
     * 机型
     */
    public enum PhoneType {
        iPhone,
        AndroidPhone,
        WindowsPhone
    }

    public static String[] PhoneTypeString = new String[]{"iPhone", "AndroidPhone", "WindowsPhone"};

    public static String[] VerifyCodeTypeString = new String[]{"register", "resetPassword", "saveServicePointBankAccountInfo"};

    /**
     * 验证码类型
     */
    public enum VerifyCodeType {
        register,
        resetPassword,
        saveServicePointBankAccountInfo
    }

    /**
     * 用户信息类型
     */
    public enum UserInfoType {
        All,
        Base,
        Detail
    }

    public static String[] UserInfoTypeString = new String[]{"All", "Base", "Detail"};
}

