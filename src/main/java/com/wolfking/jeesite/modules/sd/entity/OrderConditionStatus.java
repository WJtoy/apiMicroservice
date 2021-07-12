package com.wolfking.jeesite.modules.sd.entity;

import lombok.Data;

/**
 * @Auther wj
 * @Date 2021/3/15 11:05
 */
@Data
public class OrderConditionStatus {
    public static final int ORDER_STATUS_INIT = 20;
    public static final int ORDER_STATUS_FOUR = 40;
    public static final int ORDER_STATUS_OTHER = 50;
    public static final int ORDER_STATUS_END = 55;


    private Integer status = 0;

    private Long servicepointId = 0L;

    private Long engineerId = 0L;



}
