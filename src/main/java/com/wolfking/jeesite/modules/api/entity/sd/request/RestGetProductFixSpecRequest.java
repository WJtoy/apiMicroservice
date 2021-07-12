package com.wolfking.jeesite.modules.api.entity.sd.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 产品安装规范请求参数
 */
public class RestGetProductFixSpecRequest {

    /**
     * 工单ID
     */
    @Getter
    @Setter
    private Long orderId = 0L;

    /**
     * 工单分片标识
     */
    @Getter
    @Setter
    private String quarter = "";

    /**
     * 产品ID
     */
    @Getter
    @Setter
    private Long productId = 0L;

    /**
     * 工单子项目的序号
     */
    @Getter
    @Setter
    private Integer orderItemIndex = 0;
}
