package com.wolfking.jeesite.modules.sd.utils;

import com.wolfking.jeesite.common.utils.SpringContextHolder;
import com.wolfking.jeesite.modules.sd.entity.OrderCacheParam;
import com.wolfking.jeesite.modules.sd.service.OrderCacheService;

public class OrderCacheUtils {

    private static OrderCacheService orderCacheService = SpringContextHolder.getBean(OrderCacheService.class);

    /**
     * 更新工单缓存
     */
    public static boolean update(OrderCacheParam param) {
        return orderCacheService.update(param);
    }

    /**
     * 清空整个工单的缓存
     */
    public static boolean delete(Long orderId) {
        return orderCacheService.delete(orderId);
    }

    /**
     * 订单上门服务变更标志
     */
    public static boolean setDetailActionFlag(Long orderId){
        return orderCacheService.setDetailActionFlag(orderId);
    }
}
