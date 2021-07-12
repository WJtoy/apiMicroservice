package com.wolfking.jeesite.modules.md.entity.viewModel;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;

import java.util.Date;

/**
 * @Auther wj
 * @Date 2021/1/14 14:41
 */
public class EngineerTemperatureSearchModel extends LongIDDataEntity<EngineerTemperatureSearchModel> {


    private Long engineerId;
    private Integer limitOffset = 1;
    private Integer limitRows = 10;
    private Date beginCreateDate;
    private Date endCreateDate;
    private String quarter;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Date getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(Date beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public Long getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Long engineerId) {
        this.engineerId = engineerId;
    }

    public Integer getLimitOffset() {
        return limitOffset;
    }

    public void setLimitOffset(Integer limitOffset) {
        this.limitOffset = limitOffset;
    }

    public Integer getLimitRows() {
        return limitRows;
    }

    public void setLimitRows(Integer limitRows) {
        this.limitRows = limitRows;
    }


}
