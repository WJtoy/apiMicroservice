package com.wolfking.jeesite.modules.sys.entity;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 客服类型
 * @date 2019/12/11 6:02 下午
 */
public enum KefuTypeEnum {

    SuperKefu(0,"超级客服"),
    VIPKefu(1,"VIP客服"),
    Kefu(2,"普通客服"),
    Rush(3,"突击客服");

    public Integer code;
    public String msg;

    private KefuTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static KefuTypeEnum fromCode(int code) {
        for (KefuTypeEnum type : KefuTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}
