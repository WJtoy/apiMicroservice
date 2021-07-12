package com.wolfking.jeesite.modules.md.entity;

import com.google.common.collect.Maps;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.util.Map;

/**
 * 网点状态枚举类
 */
public enum ServicePointStatus {

    NORMAL(10, "正常"),
    PAUSED(20, "暂停派单"),
    CANCELLED(30, "取消合作"),
    BLACKLIST(100, "黑名单");

    private int value;
    private String label;

    ServicePointStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    private static final Map<Integer, ServicePointStatus> map = Maps.newHashMap();

    static {
        for (ServicePointStatus item : ServicePointStatus.values()) {
            map.put(item.value, item);
        }
    }

    public static Dict createDict(ServicePointStatus status) {
        if (status == null) {
            return new Dict(0, "");
        } else {
            return new Dict(status.getValue(), status.getLabel());
        }
    }

    public static ServicePointStatus valueOf(Integer value) {
        ServicePointStatus status = null;
        if (value != null) {
            status = map.get(value);
        }
        return status;
    }

}
