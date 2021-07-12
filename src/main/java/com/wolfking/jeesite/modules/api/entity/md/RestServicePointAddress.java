package com.wolfking.jeesite.modules.api.entity.md;

/**
 * 网点地址信息
 */
public class RestServicePointAddress {

    private String address = "";//详细地址（省市区街道门牌号）
    private Double longitude = 0.0D;//经度（高德坐标）
    private Double latitude = 0.0D;//维度（高德坐标）
    private Long areaId = 0L;//区域ID

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }
}
