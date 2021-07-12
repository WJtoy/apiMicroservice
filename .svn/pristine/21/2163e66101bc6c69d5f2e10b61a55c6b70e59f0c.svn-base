package com.wolfking.jeesite.ms.logistics.service;

import com.google.common.collect.Maps;
import com.kkl.kklplus.entity.lm.mq.MQLMExpress;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.service.OrderMaterialService;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

/**
 * 物流服务层
 *
 * @author Ryan Lu
 * @date 2019/5/27 10:07 AM
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LogisticsService {

    private static final User user = new User(5L,"外部接口","");
    //private static final SequenceIdUtils sequenceIdUtils = new SequenceIdUtils(ThreadLocalRandom.current().nextInt(32),ThreadLocalRandom.current().nextInt(32));

//    @Value("${logistics.orderFlag}")
//    private boolean orderFlag;//订单物流开关

//    @Value("${logistics.materialFlag}")
//    private boolean materialFlag;//配件单物流开关

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMaterialService orderMaterialService;

    /**
     * 同步物流的签收日期到订单商品的实际上门日期
     * 如订单已设定到货日期，且比物流接口提供的日期还早，就不用更新
     * @param message
     */
    @Transactional(readOnly = false)
    public void updateArrivalDate(MQLMExpress.ArrivalDateMessage message,int dataSource,Dict status,int subStatus,int pendingType) {
        if (message == null || message.getOrderId() <= 0 || StringUtils.isBlank(message.getQuarter())) {
            throw new OrderException("参数无值。");
        }
        Date date = new Date();
        Date arriveDate = DateUtils.longToDate(message.getArrivalDate());
        HashMap<String, Object> params = Maps.newHashMap();
        try {
            //condition
            params.put("orderId", message.getOrderId());
            params.put("quarter", message.getQuarter());
            params.put("arrivalDate", arriveDate);
            params.put("updateBy", user);
            params.put("updateDate", date);
            //停滞原因:等到货,subStatus = 20
            if(subStatus == Order.ORDER_SUBSTATUS_PENDING.intValue() && pendingType == 4){
                params.put("pendingTypeDate", date);
                params.put("appointmentDate", date);
                params.put("reservationDate", date);
            }
            orderService.updateOrderCondition(params);

            // log
            OrderProcessLog log = new OrderProcessLog();
            log.setQuarter(message.getQuarter());
            log.setAction("物流接口同步到货日期信息");
            log.setOrderId(message.getOrderId());
            log.setActionComment("物流接口同步到货日期信息:" + DateUtils.formatDate(arriveDate, "yyyy-MM-dd HH:mm"));
            log.setStatus(status.getLabel());
            log.setStatusValue(Integer.parseInt(status.getValue()));
            log.setStatusFlag(OrderProcessLog.OPL_SF_TRACKING);
            log.setCloseFlag(0);
            log.setCreateBy(user);
            log.setCreateDate(date);
            log.setRemarks(log.getActionComment());//厂家可见
            log.setDataSourceId(dataSource);
            orderService.saveOrderProcessLogNew(log);

        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            throw new OrderException(e);
        }
    }

    /**
     * 配件签收
     */
    @Transactional
    public void updatePartsArrivalDate(MQLMExpress.ArrivalDateMessage message,int dataSource,Dict status,int subStatus){
        if (message == null || message.getOrderId() <= 0 || StringUtils.isBlank(message.getQuarter())) {
            throw new OrderException("参数无值。");
        }
        Date date = new Date();
        Date arriveDate = DateUtils.longToDate(message.getArrivalDate());
        HashMap<String, Object> params = Maps.newHashMap();
        try {
            //condition, subStatus = 30,等配件
            if(subStatus == Order.ORDER_SUBSTATUS_WAITINGPARTS.intValue()) {
                params.put("orderId", message.getOrderId());
                params.put("quarter", message.getQuarter());
                params.put("updateBy", user);
                params.put("updateDate", date);
                params.put("pendingTypeDate", date);
                params.put("appointmentDate", date);
                params.put("reservationDate", date);
                orderService.updateOrderCondition(params);
            }
            //更新配件签收日期
            orderMaterialService.updateLogisticSignAt(message.getOrderId(),message.getQuarter(),message.getExpressNo(),arriveDate);
            // log
            OrderProcessLog log = new OrderProcessLog();
            log.setQuarter(message.getQuarter());
            log.setAction("物流接口同步配件到货信息");
            log.setOrderId(message.getOrderId());
            log.setActionComment("物流接口同步配件到货信息，快递单号:" + message.getExpressNo() + " ,到货时间:" + DateUtils.formatDate(arriveDate, "yyyy-MM-dd HH:mm"));
            log.setStatus(status.getLabel());
            log.setStatusValue(status.getIntValue());
            log.setStatusFlag(OrderProcessLog.OPL_SF_TRACKING);
            log.setCloseFlag(0);
            log.setCreateBy(user);
            log.setCreateDate(date);
            log.setRemarks(log.getActionComment());//厂家可见
            log.setDataSourceId(dataSource);
            orderService.saveOrderProcessLogNew(log);

        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            throw new OrderException(e);
        }
    }

}
