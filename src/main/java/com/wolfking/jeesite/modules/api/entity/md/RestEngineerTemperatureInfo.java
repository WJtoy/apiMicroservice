package com.wolfking.jeesite.modules.api.entity.md;

import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.util.Date;
import java.util.Map;

/**
 * @Auther wj
 * @Date 2021/1/13 15:21
 */
public class RestEngineerTemperatureInfo {

    private Long id;
    private Double temperature = 0.0;
    private AppDict healthOption;
    private AppDict healthStatus;
    private Date createDate;

    public AppDict getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(AppDict healthStatus) {
        this.healthStatus = healthStatus;
    }


    public AppDict getHealthOption() {
        return healthOption;
    }

    public void setHealthOption(AppDict healthOption) {
        this.healthOption = healthOption;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}
