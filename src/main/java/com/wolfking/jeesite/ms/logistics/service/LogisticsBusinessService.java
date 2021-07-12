package com.wolfking.jeesite.ms.logistics.service;

import com.google.common.collect.Maps;
import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.lm.mq.MQLMExpress;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.exception.OrderException;
import com.wolfking.jeesite.common.service.SequenceIdService;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.md.entity.Product;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.service.OrderService;
import com.wolfking.jeesite.modules.sd.utils.OrderCacheUtils;
import com.wolfking.jeesite.modules.sd.utils.OrderUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import com.wolfking.jeesite.ms.logistics.mq.sender.SubsExpressMQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 物流业务层
 *
 * @author Ryan Lu
 * @date 2019/5/27 10:07 AM
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class LogisticsBusinessService {

    private static final User user = new User(5l,"外部接口","");
    //private static final SequenceIdUtils sequenceIdUtils = new SequenceIdUtils(ThreadLocalRandom.current().nextInt(32),ThreadLocalRandom.current().nextInt(32));
    @Autowired
    private SequenceIdService sequenceIdService;
    


    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubsExpressMQSender sender;

    @Autowired
    private LogisticsService logisticsService;

//    @Value("${logistics.orderFlag}")
    private boolean orderFlag;//订单物流开关

//    @Value("${logistics.materialFlag}")
    private boolean materialFlag;//配件单物流开关

    @Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.orderFlag = webProperties.getLogistics().getOrderFlag();
        this.materialFlag = webProperties.getLogistics().getMaterialFlag();
    }

    /**
     * 同步物流的签收日期到订单商品的实际上门日期
     * 如订单已设定到货日期，且比物流接口提供的日期还早，就不用更新
     * @param message
     */
    public void updateArrivalDate(MQLMExpress.ArrivalDateMessage message) {
        if (message == null || message.getOrderId() <= 0 || StringUtils.isBlank(message.getQuarter())) {
            throw new OrderException("参数无值。");
        }
        //检查锁
        String lockKey = String.format(RedisConstant.SD_ORDER_LOCK, message.getOrderId());
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 30);//30秒
        if (!locked) {
            throw new OrderException("该单正在被其他人处理中，请稍候重试，或刷新页面。");
        }
        try {
            Order o = orderService.getOrderById(message.getOrderId(), message.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (o == null || o.getOrderCondition() == null) {
                throw new OrderException("订单读取失败，请稍候重试。");
            }
            OrderCondition condition = o.getOrderCondition();
            if (condition.getArrivalDate() != null && condition.getArrivalDate().getTime() <= message.getArrivalDate()) {
                throw new OrderException("该单已设置了到货日期，且比要同步日期更早。");
            }
            condition.setArrivalDate(DateUtils.longToDate(message.getArrivalDate()));
            logisticsService.updateArrivalDate(message,o.getDataSourceId(),condition.getStatus(),condition.getSubStatus(),condition.getPendingType().getIntValue());
            //调用公共缓存
            OrderCacheParam.Builder builder = new OrderCacheParam.Builder();
            builder.setOpType(OrderCacheOpType.UPDATE)
                    .setOrderId(message.getOrderId())
                    .incrVersion(1L)
                    .setCondition(condition)
                    .setExpireSeconds(0L);
            OrderCacheUtils.update(builder.build());
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

    /**
     * 配件签收
     */
    public void updatePartsArrivalDate(MQLMExpress.ArrivalDateMessage message){
        if (message == null || message.getOrderId() <= 0 || StringUtils.isBlank(message.getQuarter())) {
            throw new OrderException("参数无值。");
        }
        //检查锁
        String lockKey = String.format(RedisConstant.SD_ORDER_LOCK, message.getOrderId());
        //获得锁
        Boolean locked = redisUtilsLocal.setNX(RedisConstant.RedisDBType.REDIS_LOCK_DB, lockKey, 1, 30);//30秒
        if (!locked) {
            throw new OrderException("该单正在被其他人处理中，请稍候重试，或刷新页面。");
        }
        try {
            Order o = orderService.getOrderById(message.getOrderId(), message.getQuarter(), OrderUtils.OrderDataLevel.CONDITION, true);
            if (o == null || o.getOrderCondition() == null) {
                throw new OrderException("订单读取失败，请稍候重试。");
            }
            OrderCondition condition = o.getOrderCondition();
            logisticsService.updatePartsArrivalDate(message,o.getDataSourceId(),condition.getStatus(),condition.getSubStatus());
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

    public void updateReturnPartsArrivalDate(MQLMExpress.ArrivalDateMessage message){
        //todo:返件到货
    }

    /**
     * 订阅订单物流接口
     */
    public void subsLogisticsMessage(MQLMExpress.GoodsType goodsType,long orderId, String orderNo, String quarter, String phone, List<OrderItem> items){
        //物流接口开关未开放
        if(goodsType == MQLMExpress.GoodsType.Goods && !orderFlag){
            return;
        }

        if(orderId<=0 || StringUtils.isBlank(orderNo) || StringUtils.isBlank(quarter)){
            return;
        }
        if(ObjectUtils.isEmpty(items)){
            return;
        }
        MQLMExpress.ExpressMessage.Builder builder = MQLMExpress.ExpressMessage.newBuilder();
        //items
        OrderItem item;
        long id;
        Map<String,String> expressNoMap = Maps.newHashMapWithExpectedSize(items.size());
        StringBuilder sb = new StringBuilder(100);
        for(int i=0,size=items.size();i<size;i++){
            item = items.get(i);
            if(StringUtils.isBlank(item.getExpressNo())){
                continue;
            }
            //防止同订单相同快递单号重复订阅
            sb.setLength(0);
            sb.append(item.getExpressNo().trim().toLowerCase()).append("::").append(item.getExpressCompany().getValue().trim().toLowerCase());
            if(expressNoMap.containsKey(sb.toString())){
                continue;
            }
            expressNoMap.put(sb.toString(),item.getExpressNo());
            //end
            id = sequenceIdService.nextId();
            builder.addItems(MQLMExpress.ExpressItem.newBuilder()
                    .setId(id)
                    .setGoodsId(item.getProductId())
                    .setGoodsName(item.getProduct().getName())
                    .setCompanyCode(item.getExpressCompany().getValue())
                    .setCompanyName(item.getExpressCompany().getLabel())
                    .setNumber(item.getExpressNo())
                    .build());
        }
        if(builder.getItemsCount() == 0){
            return;
        }
        MQLMExpress.ExpressMessage message = null;
        try {
            message = builder.setOrderId(orderId)
                    .setOrderNo(orderNo)
                    .setQuarter(quarter)
                    .setGoodsType(goodsType)
                    .setPhone(StringUtils.isBlank(phone) ? "" : phone)
                    .setCreateAt(System.currentTimeMillis())
                    .build();
            System.out.println(new JsonFormat().printToString(message));
            sender.sendRetry(message, 5000, 1);//5秒
        }catch (Exception e){
            if(message == null) {
                StringBuilder json = new StringBuilder(2000);
                json.append("{\"orderId\":").append(orderId)
                        .append("\"orderNo\":").append("\"").append(orderNo).append("\"")
                        .append("\"quarter\":").append("\"").append(quarter).append("\"")
                        .append("\"items\":").append(GsonUtils.getInstance().toGson(items));
                LogUtils.saveLog("发送快递单订阅消息错误", "LogisticsService.subsLogisticsMessage", json.toString(), e, user);
            }else {
                LogUtils.saveLog("发送快递单订阅消息错误", "LogisticsService.subsLogisticsMessage", new JsonFormat().printToString(message), e, user);
            }
        }
    }
    /**
     * 订阅配件单物流接口
     */
    public void subsPartsLogisticsMessage(MQLMExpress.GoodsType goodsType, long orderId, String orderNo, String quarter, String phone, Product product, Dict company, String expressNo){
        //非配件或物流接口开关未开放 ，忽略
        if(goodsType != MQLMExpress.GoodsType.Parts || !materialFlag){
            return;
        }

        if(orderId<=0 || StringUtils.isBlank(orderNo) || StringUtils.isBlank(quarter)){
            return;
        }
        if(product == null || company == null || StringUtils.isBlank(expressNo)){
            return;
        }
        MQLMExpress.ExpressMessage.Builder builder = MQLMExpress.ExpressMessage.newBuilder();
        //items
        OrderItem item;
        long id = sequenceIdService.nextId();
        builder.addItems(MQLMExpress.ExpressItem.newBuilder()
                .setId(id)
                .setGoodsId(product.getId())
                .setGoodsName(product.getName())
                .setCompanyCode(company.getValue())
                .setCompanyName(company.getLabel())
                .setNumber(expressNo)
                .build());
        if(builder.getItemsCount() == 0){
            return;
        }
        MQLMExpress.ExpressMessage message = null;
        try {
            message = builder.setOrderId(orderId)
                    .setOrderNo(orderNo)
                    .setQuarter(quarter)
                    .setGoodsType(goodsType)//*
                    .setPhone(StringUtils.isBlank(phone) ? "" : phone)
                    .setCreateAt(System.currentTimeMillis())
                    .build();
            //System.out.println(new JsonFormat().printToString(message));
            sender.sendRetry(message, 5000, 1);//5秒
        }catch (Exception e){
            if(message == null) {
                StringBuilder json = new StringBuilder(2000);
                json.append("{\"orderId\":").append(orderId)
                        .append("\"orderNo\":").append("\"").append(orderNo).append("\"")
                        .append("\"quarter\":").append("\"").append(quarter).append("\"")
                        .append("\"items\":{")
                        .append("\"number\":\"").append(expressNo).append("\"")
                        .append(",\"companyCode\":\"").append(company.getValue()).append("\"")
                        .append(",\"companyName\":\"").append(company.getLabel()).append("\"")
                        .append(",\"goodsId\":").append(product.getId())
                        .append(",\"goodsName\":\"").append(product.getName()).append("\"")
                        .append("}");
                LogUtils.saveLog("发送快递单订阅消息错误", "LogisticsService.subsPartsLogisticsMessage", json.toString(), e, user);
            }else {
                LogUtils.saveLog("发送快递单订阅消息错误", "LogisticsService.subsPartsLogisticsMessage", new JsonFormat().printToString(message), e, user);
            }
        }
    }
}
