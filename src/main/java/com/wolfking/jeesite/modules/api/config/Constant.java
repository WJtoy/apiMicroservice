package com.wolfking.jeesite.modules.api.config;

public class Constant {
    /**
     * jwt
     */
    public static final String JWT_AuthHeaderPrefix = "KKLAPP";
    public static final String JWT_ID = "kkl_app";
    public static final String JWT_SECRET = "3fabff41c36811e7a5ca00163e087291";//密钥
    public static final long JWT_TTL = 5 * 24 * 60 * 60 * 1000;  //登录的验证码过期时间（毫秒）5天
    public static final long wxTime = 60 * 60;

}
