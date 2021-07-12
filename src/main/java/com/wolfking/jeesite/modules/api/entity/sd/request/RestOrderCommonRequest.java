package com.wolfking.jeesite.modules.api.entity.sd.request;

import lombok.Getter;
import lombok.Setter;

public class RestOrderCommonRequest {

    /**
     * 工单ID
     */
    @Getter
    @Setter
    private Long orderId;
    /**
     * 数据库分片
     */
    @Getter
    @Setter
    private String quarter;
}
