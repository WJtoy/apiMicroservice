package com.wolfking.jeesite.ms.b2bcenter.sd.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.common.exception.MSErrorCode;
import com.kkl.kklplus.common.response.MSResponse;
import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BCenterOrderExpressArrivalMessage;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sd.entity.OrderProcessLog;
import com.wolfking.jeesite.modules.sd.service.OrderCacheReadService;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.entity.VisibilityFlagEnum;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;

import static com.wolfking.jeesite.modules.sd.utils.OrderUtils.ORDER_LOCK_EXPIRED;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Service
public class B2BCenterOrderExpressArrivalService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderCacheReadService orderCacheReadService;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    /**
     * 处理修改快可立工单的消息
     */
    public MSResponse processOrderExpressArrivalMessage(MQB2BCenterOrderExpressArrivalMessage.B2BCenterOrderExpressArrivalMessage message, User user) {
        MSResponse response = new MSResponse(MSErrorCode.SUCCESS);
//        B2BDataSourceEnum dataSourceEnum = B2BDataSourceEnum.valueOf(message.getDataSource());
        try {
//                if (B2BDataSourceEnum.TMALL == dataSourceEnum) {
            if (B2BDataSourceEnum.isB2BDataSource(message.getDataSource())) {
                updateArrivalDate(message, user);
            }
//                }
        } catch (Exception e) {
            String msgJson = new JsonFormat().printToString(message);
            LogUtils.saveLog("处理B2B工单到货消息", "B2BCenterOrderExpressArrivalService.processOrderExpressArrivalMessage", msgJson, e, null);
            response.setCode(MSErrorCode.FAILURE.getCode());
        }
        return response;
    }


    public void updateArrivalDate(MQB2BCenterOrderExpressArrivalMessage.B2BCenterOrderExpressArrivalMessage message, User user) {
        if (message == null || message.getKklOrderId() == 0 || message.getArrivalTime() == 0) {
            throw new OrderException("参数无值。");
        }
        String lockKey = String.format(RedisConstant.SD_ORDER_LOCK, message.getKklOrderId());
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, ORDER_LOCK_EXPIRED);
        if (!locked) {
            throw new OrderException("此订单正在处理中，请稍候重试，或刷新页面。");
        }

        Date date = new Date();
        Date arriveDate = DateUtils.longToDate(message.getArrivalTime());
        HashMap<String, Object> params = Maps.newHashMap();
        try {
            Order order = orderCacheReadService.getOrderById(message.getKklOrderId(), null, OrderUtils.OrderDataLevel.CONDITION, true, true);
            if (order == null || order.getOrderCondition() == null) {
                throw new OrderException("读取订单失败，请确认订单时候存在.");
            }
            Dict status = order.getOrderCondition().getStatus();
            params.put("orderId", order.getId());
            params.put("quarter", order.getQuarter());
            params.put("arrivalDate", arriveDate);
            params.put("updateBy", user);
            params.put("updateDate", date);

            OrderProcessLog log = new OrderProcessLog();
            log.setQuarter(order.getQuarter());
            log.setAction("B2B接口同步到货日期信息");
            log.setOrderId(order.getId());
            log.setStatus(status.getLabel());
            log.setStatusValue(Integer.parseInt(status.getValue()));
            log.setStatusFlag(OrderProcessLog.OPL_SF_TRACKING);
            log.setCloseFlag(0);
            log.setCreateBy(user);
            log.setCreateDate(date);
            log.setDataSourceId(order.getDataSourceId());
            StringBuilder actionComment = new StringBuilder();
            actionComment.append("B2B接口同步到货日期信息:").append(DateUtils.formatDate(arriveDate, "yyyy-MM-dd HH:mm:ss"));
            if (Order.ORDER_SUBSTATUS_PENDING.equals(order.getOrderCondition().getSubStatus()) && order.getOrderCondition().getPendingType().getIntValue() == 4) {
                params.put("pendingTypeDate", date);
                params.put("appointmentDate", date);
                params.put("reservationDate", date);
                actionComment.append("【到货通知，请及时跟进】");
            }
            log.setActionComment(actionComment.toString());
            orderService.updateOrderCondition(params);
//            orderService.saveOrderProcessLogNew(log);
            //所有接口获取到的到货信息都要显示给厂商（原来是客服、网点可见）
            log.setVisibilityFlag(VisibilityFlagEnum.or(Sets.newHashSet(VisibilityFlagEnum.KEFU, VisibilityFlagEnum.SERVICE_POINT, VisibilityFlagEnum.CUSTOMER)));
            orderService.saveOrderProcessLogWithNoCalcVisibility(log);
            OrderCacheUtils.delete(order.getId());
        } catch (OrderException oe) {
            throw oe;
        } catch (Exception e) {
            throw new OrderException(e);
        } finally {
            if (locked && lockKey != null) {
                redisUtilsLocal.remove(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey);
            }
        }
    }

}
