package com.wolfking.jeesite.modules.sd.entity;

/**
 * 质保类型
 */
public enum WarrantyType {

    IW("IW","保内"),
    OOT("OOT","保外");

    public String code;
    public String msg;

    private WarrantyType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static WarrantyType fromCode(String code) {
        for (WarrantyType type : WarrantyType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}

