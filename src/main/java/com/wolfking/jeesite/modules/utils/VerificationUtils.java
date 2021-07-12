package com.wolfking.jeesite.modules.utils;

import com.wolfking.jeesite.common.utils.SpringContextHolder;

import com.wolfking.jeesite.modules.api.entity.sd.RestOrderCondition;
import com.wolfking.jeesite.modules.sd.entity.OrderConditionStatus;
import com.wolfking.jeesite.modules.sd.service.OrderConditionService;



/**
 * @Auther wj
 * @Date 2021/3/9 11:03
 */
public class VerificationUtils {

    private static OrderConditionService  orderConditionService = SpringContextHolder.getBean(OrderConditionService.class);

    public static boolean compare(Long orderId, String quarter,Integer opCode,Long engineerId,Long servicePointId){
        OrderConditionStatus orderConditionStatus = orderConditionService.getOrderInfo(orderId,quarter);
        if (orderConditionStatus != null ) {
                boolean bool1 = compareStatus(orderConditionStatus.getStatus(), opCode);
                boolean bool2 = compareId(orderConditionStatus.getEngineerId(), engineerId);
                boolean bool3 = compareId(orderConditionStatus.getServicepointId(), servicePointId);
                return bool1 && bool2 && bool3;
        }
        return false;
    }

    public static boolean compareStatusAndPointId(Long orderId, String quarter,Integer opCode,Long servicePointId){
        OrderConditionStatus orderConditionStatus = orderConditionService.getOrderInfo(orderId,quarter);
        if (orderConditionStatus != null ) {
            boolean bool1 = compareStatus(orderConditionStatus.getStatus(), opCode);
            boolean bool2 = compareId(orderConditionStatus.getServicepointId(), servicePointId);
            return bool1 && bool2 ;
        }
        return false;
    }


    //比较工单的状态
    public static boolean compareOrderStatus(Long orderId,String quarter,Integer opCode){
        OrderConditionStatus orderConditionStatus = orderConditionService.getOrderInfo(orderId,quarter);
        if (orderConditionStatus != null && orderConditionStatus.getStatus() != null){
           return compareStatus(orderConditionStatus.getStatus(),opCode);
        }
        return false;
    }





    public static boolean compareStatus(Integer status,Integer opCode){
        if (opCode==OperationCommand.OperationCode.GRAB.code){
            if (status>=OrderConditionStatus.ORDER_STATUS_INIT && status<OrderConditionStatus.ORDER_STATUS_FOUR){
                return true;
            }
        }else if (opCode==OperationCommand.OperationCode.APP_CLOSE.code){
            if (status>=OrderConditionStatus.ORDER_STATUS_OTHER && status<OrderConditionStatus.ORDER_STATUS_END){
                return true;
            }
        }else {
            if (status>=OrderConditionStatus.ORDER_STATUS_FOUR && status<OrderConditionStatus.ORDER_STATUS_END){
                return true;
            }
        }
        return false;
    }

    public static boolean compareId(Long targetId,Long sourceId){
        if (targetId.equals(sourceId)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        VerificationUtils.compareId(0L,0L);
    }


}
