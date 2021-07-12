package com.wolfking.jeesite.modules.api.entity.sd;

import lombok.Data;

/**
 * @Auther wj
 * @Date 2021/3/17 15:15
 */
@Data
public class RestOrderCondition {
    private Long orderId;
    private String quarter;
    private Integer opCode;
    private Long engineerId;
    private Long servicePointId;
}
