/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.wolfking.jeesite.modules.sd.service;

import com.wolfking.jeesite.common.service.BaseService;
import com.wolfking.jeesite.modules.sd.dao.OrderAdditionalInfoDao;
import com.wolfking.jeesite.modules.sd.dao.OrderHeadDao;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderAdditionalInfo;
import com.wolfking.jeesite.modules.sd.utils.OrderAdditionalInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Configurable
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Slf4j
@RequiredArgsConstructor
public class OrderAdditionalInfoService extends BaseService {

    private final OrderAdditionalInfoDao additionalInfoDao;
    private final OrderHeadDao orderHeadDao;

    public OrderAdditionalInfo getOrderAdditionalInfo(Long orderId, String quarter) {
        OrderAdditionalInfo additionalInfo = null;
        if (orderId > 0) {
            //2020-12-19 sd_order -> sd_order_head
            Order order = additionalInfoDao.getOrderAdditionalInfo(orderId, quarter);
            if(order != null && order.getAdditionalInfoPb() != null && order.getAdditionalInfoPb().length >0){
                additionalInfo = OrderAdditionalInfoUtils.pbBypesToAdditionalInfo(order.getAdditionalInfoPb());
            }
        }
        return additionalInfo;
    }

    /**
     * 获取工单的购买时间
     */
    public Long getBuyDate(Long orderId, String quarter) {
        Long buyDate = null;
        OrderAdditionalInfo additionalInfo = getOrderAdditionalInfo(orderId, quarter);
        if (additionalInfo != null && additionalInfo.getBuyDate() != null && additionalInfo.getBuyDate() > 0) {
            buyDate = additionalInfo.getBuyDate();
        }
        return buyDate;
    }

    @Transactional()
    public void updateBuyDate(Long orderId, String quarter, Long buyDate) {
        if (orderId == null || orderId == 0 || buyDate == null || buyDate == 0) {
            throw new RuntimeException("修改购买时间的参数格式不正确");
        }
        OrderAdditionalInfo additionalInfo = getOrderAdditionalInfo(orderId, quarter);
        if (additionalInfo == null) {
            additionalInfo = new OrderAdditionalInfo();
        }
        long oldBuyDate = additionalInfo.getBuyDate() == null ? 0 : additionalInfo.getBuyDate();
        if (Math.abs(oldBuyDate - buyDate) > 60 * 1000) {
            additionalInfo.setBuyDate(buyDate);
            byte[] addtionInfoPb = OrderAdditionalInfoUtils.additionalInfoToPbBytes(additionalInfo);
            orderHeadDao.updateOrderAdditionalInfo(orderId,quarter,addtionInfoPb);//2020-12-03 sd_order -> sd_order_head
        }
    }
}
