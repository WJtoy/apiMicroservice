package com.wolfking.jeesite.ms.praise.service;

import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.praise.Praise;
import com.wolfking.jeesite.common.service.LongIDBaseService;
import com.wolfking.jeesite.ms.praise.feign.OrderPraiseFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderPraiseService extends LongIDBaseService {

    @Autowired
    private OrderPraiseFeign orderPraiseFeign;

    /**
     * 根据订单Id，网点Id获取好评费
     */
    public Praise getByOrderId(String quarter, Long orderId, Long servicePointId) {
        MSResponse<Praise> msResponse = orderPraiseFeign.getByOrderIdAndServicepointId(quarter, orderId, servicePointId);
        if (MSResponse.isSuccess(msResponse)) {
            return msResponse.getData();
        } else {
            return null;
        }
    }

}
