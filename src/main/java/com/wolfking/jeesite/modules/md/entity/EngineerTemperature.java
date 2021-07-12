package com.wolfking.jeesite.modules.md.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

import java.util.Date;

/**
 * @Auther wj
 * @Date 2021/1/13 11:07
 */
public class EngineerTemperature  extends LongIDDataEntity<EngineerTemperature> {

    private static final long serialVersionUID = 1L;

    private Long engineerId;

    private double temperature = 0.0;

    private Integer healthOption;

    private Integer healthStatus;

    private Long createById;

    private Long updateById;
    private String quarter;

    public Long getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
    }



    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }



    public Integer getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(Integer healthStatus) {
        this.healthStatus = healthStatus;
    }





    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }


    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Integer getHealthOption() {
        return healthOption;
    }

    public void setHealthOption(Integer healthOption) {
        this.healthOption = healthOption;
    }


}
