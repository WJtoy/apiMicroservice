package com.wolfking.jeesite.modules.api.entity.md;

import com.wolfking.jeesite.modules.api.util.RestEnum;

/**
 * 登录用户相关信息
 */
public class RestLoginUserInfo {
    private Long userId;
    private Long servicePointId;
    private long engineerId;
    private RestEnum.PhoneType phoneType;
    private Boolean isPrimary = false;//主帐号

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(Long servicePointId) {
        this.servicePointId = servicePointId;
    }

    public long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(long engineerId) {
        this.engineerId = engineerId;
    }

    public RestEnum.PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(RestEnum.PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }
}
