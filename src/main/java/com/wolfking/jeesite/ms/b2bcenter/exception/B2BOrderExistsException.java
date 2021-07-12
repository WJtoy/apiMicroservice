package com.wolfking.jeesite.ms.b2bcenter.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

public class B2BOrderExistsException extends RuntimeException {

    @Getter
    @Setter
    private Long orderId = 0L;

    @Getter
    @Setter
    private String orderNo = "";

    @Getter
    @Setter
    private Integer dataSourceId = 0;

    @Getter
    @Setter
    private String b2bOrderNo = "";

    public B2BOrderExistsException(String msg, Long orderId, String orderNo, Integer dataSourceId, String b2bOrderNo) {
        super(msg);
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.dataSourceId = dataSourceId;
        this.b2bOrderNo = b2bOrderNo;
    }

}
