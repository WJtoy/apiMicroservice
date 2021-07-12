package com.wolfking.jeesite.modules.sd.service;

import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.GsonRedisUtils;
import com.wolfking.jeesite.common.utils.StringUtils2;
import com.wolfking.jeesite.modules.sd.entity.*;
import com.wolfking.jeesite.modules.sd.utils.OrderConditionRedisAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderRedisAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OrderCacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    // ---------------------------------------------------------------------------------------------------------

    //region 更新/删除

    /**
     * 更新工单缓存
     */
    public boolean update(OrderCacheParam param) {
        boolean result = true;
        if (param != null && StringUtils.isNotBlank(param.getOrderCacheKey())) {
            if (param.getOpType() == OrderCacheOpType.DELETE_ALL) {
                result = deleteRedis(param.getOrderCacheKey());
            } else {
                if ((param.getDeleteParams() != null && !param.getDeleteParams().isEmpty())
                        || (param.getUpdateParams() != null && !param.getUpdateParams().isEmpty())
                        || (param.getIncrementParams() != null && !param.getIncrementParams().isEmpty())) {
                    result = updateRedis(param);
                }
            }
            if (!result) {
                deleteRedis(param.getOrderCacheKey());
                //LogUtils.saveLog("OrderCacheUtils", "update", param.getOrderCacheKey(), null, null);
            }
        }

        return result;
    }

    /**
     * 清空整个工单的缓存
     */
    public boolean delete(Long orderId) {
        boolean result = true;
        if (orderId != null && orderId > 0) {
            String orderCacheKey = String.format(RedisConstant.SD_ORDER, orderId);
            result = deleteRedis(orderCacheKey);
            if (!result) {
                deleteRedis(orderCacheKey);
                //LogUtils.saveLog("OrderCacheUtils", "delete", orderCacheKey, null, null);
            }
        }
        return result;
    }

    /**
     * 删除工单的Redis缓存
     */
    private boolean deleteRedis(String orderCacheKey) {
        boolean result = true;
        final byte[] bKey = orderCacheKey.getBytes(StandardCharsets.UTF_8);
        try {
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_SD_DB.ordinal());
                boolean isExists = connection.exists(bKey);
                if (isExists) {
                    connection.del(bKey);
                }
                return true;
            });
        } catch (Exception e) {
            result = false;
            //LogUtils.saveLog("OrderCacheUtils", "deleteRedis", orderCacheKey, e, null);
        }
        return result;
    }

    /**
     * 更新工单的Redis缓存
     */
    private boolean updateRedis(OrderCacheParam param) {
        boolean result = true;
        try {
            final Map<byte[], Long> bIncrFieldValues = getIncrFieldValueBytes(param);
            final Map<byte[], byte[]> bUpdateFieldValues = getUpdateFieldValueBytes(param);
            final byte[][] bDeleteFields = getDeleteFieldBytes(param);
            final byte[] bKey = param.getOrderCacheKey().getBytes(StandardCharsets.UTF_8);

            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_SD_DB.ordinal());
                boolean isExists = connection.exists(bKey);

                connection.multi();
                if (isExists) {
                    //执行增量操作
                    if (bIncrFieldValues != null && !bIncrFieldValues.isEmpty()) {
                        for (Map.Entry<byte[], Long> incrItem : bIncrFieldValues.entrySet()) {
                            connection.hIncrBy(bKey, incrItem.getKey(), incrItem.getValue());
                        }
                    }
                    //执行删除操作
                    if (bDeleteFields != null && bDeleteFields.length > 0) {
                        connection.hDel(bKey, bDeleteFields);
                    }
                }
                //执行更新操作
                if (bUpdateFieldValues != null && !bUpdateFieldValues.isEmpty()) {
                    connection.hMSet(bKey, bUpdateFieldValues);
                    if (param.getExpireSeconds() > 0) {
                        connection.expire(bKey, param.getExpireSeconds());
                    }
                }
                connection.exec();
                return true;
            });
        } catch (Exception e) {
            result = false;
            //LogUtils.saveLog("OrderCacheUtils", "updateRedis", param.getOrderCacheKey(), e, null);
        }
        return result;
    }

    private byte[][] getDeleteFieldBytes(OrderCacheParam param) {
        byte[][] bFieldNames = null;
        if (param.getDeleteParams() != null && !param.getDeleteParams().isEmpty()) {
            List<String> nameList = param.getDeleteParams().stream().filter(Objects::nonNull).map(OrderCacheField::getName)
                    .distinct().collect(Collectors.toList());
            if (!nameList.isEmpty()) {
                bFieldNames = new byte[nameList.size()][];
                for (int i = 0; i < nameList.size(); i++) {
                    bFieldNames[i] = nameList.get(i).getBytes(StandardCharsets.UTF_8);
                }
            }
        }
        return bFieldNames;
    }


    private Map<byte[], byte[]> getUpdateFieldValueBytes(OrderCacheParam param) throws SerializationException {
        Map<byte[], byte[]> bFieldValue = Maps.newHashMap();
        if (param.getUpdateParams() != null && !param.getUpdateParams().isEmpty()) {
            byte[] bfield;
            byte[] bValue;
            Object obj;
            for (Map.Entry<OrderCacheField, Object> item : param.getUpdateParams().entrySet()) {
                bfield = item.getKey().getName().getBytes(StandardCharsets.UTF_8);
                if (item.getKey() == OrderCacheField.CONDITION) {
                    obj = OrderConditionRedisAdapter.getInstance().toJson((OrderCondition) item.getValue());
                } else if (item.getKey() == OrderCacheField.INFO) {
                    obj = OrderRedisAdapter.getInstance().toJson((Order) item.getValue());
                } else {
                    obj = item.getValue();
                }
                bValue = GsonRedisUtils.toBytes(obj);
                bFieldValue.put(bfield, bValue);
            }
        }
        return bFieldValue;
    }

    private Map<byte[], Long> getIncrFieldValueBytes(OrderCacheParam param) {
        Map<byte[], Long> bFieldValue = Maps.newHashMap();
        if (param.getIncrementParams() != null && !param.getIncrementParams().isEmpty()) {
            byte[] bfield;
            for (Map.Entry<OrderCacheField, Long> item : param.getIncrementParams().entrySet()) {
                bfield = item.getKey().getName().getBytes(StandardCharsets.UTF_8);
                bFieldValue.put(bfield, item.getValue());
            }
        }
        return bFieldValue;
    }

    /**
     * 上门服务更改标记
     * 新增/删除/确认上门等操作上门服务后，缓存中增加此标记，有效期：RedisConstant.SD_ORDER_DETAIL_FLAG_TIMEOUT 单位：秒
     * 在此期间读取上门服务，读取数据库主库(OrderCacheReadService.getOrderFromCacheAndDB()方法)
     * @param orderId   订单id
     * @return  布尔类型 true:成功  false:失败
     */
    public boolean setDetailActionFlag(Long orderId){
        if(orderId == null || orderId <= 0){
            return false;
        }
        boolean result = true;
        try {

            final byte[] bKey = String.format(RedisConstant.SD_ORDER_DETAIL_FLAG,orderId).getBytes(StandardCharsets.UTF_8);
            redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_LOCK_DB.ordinal());
                connection.setEx(bKey, RedisConstant.SD_ORDER_DETAIL_FLAG_TIMEOUT, GsonRedisUtils.toBytes(System.currentTimeMillis()));
                return true;
            });
        } catch (Exception e) {
            result = false;
            //LogUtils.saveLog("OrderCacheUtils", "setDetailActionFlag", orderId.toString(), e, null);
        }
        return result;
    }
    //endregion 更新/删除

    // ---------------------------------------------------------------------------------------------------------

    //region 读取

    /**
     * 获取工单缓存的所有信息
     */
    public OrderCacheResult getOrderAllInfo(Long orderId) {
        OrderCacheResult result = null;
        if (orderId != null && orderId > 0) {
            String orderCacheKey = String.format(RedisConstant.SD_ORDER, orderId);
            try {
                result = toOrderCacheResult(getAllFromRedis(orderCacheKey));
            } catch (Exception e) {
                log.error("读取订单缓存错误，orderId:{}",orderId,e);
            }
        }
        return result;
    }

    /**
     * 获取工单缓存的部分信息
    public OrderCacheResult getOrderPartialInfo(Long orderId, List<OrderCacheField> fields) {
        OrderCacheResult result = null;
        if (orderId != null && orderId > 0 && fields != null && !fields.isEmpty()) {
            String orderCacheKey = String.format(RedisConstant.SD_ORDER, orderId);
            List<String> fieldNames = fields.stream().filter(Objects::nonNull)
                    .map(OrderCacheField::getName).collect(Collectors.toList());
            if (!fieldNames.isEmpty()) {
                try {
                    result = toOrderCacheResult(getPartialFromRedis(orderCacheKey, fieldNames));
                } catch (Exception e) {
                    //LogUtils.saveLog("OrderCacheUtils", "getOrderPartialInfo", orderCacheKey, e, null);
                }
            }
        }
        return result;
    }
    */

    private OrderCacheResult toOrderCacheResult(Map<String, byte[]> resultMap) throws IOException {
        if (resultMap == null || resultMap.isEmpty()) {
            return null;
        }
        OrderCacheResult.Builder builder = new OrderCacheResult.Builder();
        for (Map.Entry<String, byte[]> item : resultMap.entrySet()) {
            OrderCacheField field = OrderCacheField.get(item.getKey());
            if (field != null) {
                switch (field) {
                    case VERSION:
                        Long version = GsonRedisUtils.fromBytes(item.getValue(), Long.class);
                        builder.setVersion(version);
                        break;
                    case APPOINTMENT_DATE:
                        Date appointmentDate = GsonRedisUtils.fromBytes(item.getValue(), Date.class);
                        builder.setAppointmentDate(appointmentDate);
                        break;
                    case RESERVATION_DATE:
                        Date reservationDate = GsonRedisUtils.fromBytes(item.getValue(), Date.class);
                        builder.setReservationDate(reservationDate);
                        break;
                    case RESERVATION_TIMES:
                        Integer reservationTimes = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setReservationTimes(reservationTimes);
                        break;
                    case WRITE_TIME:
                        String writeTime = GsonRedisUtils.fromBytes(item.getValue(), String.class);
                        builder.setWriteTime(writeTime);
                        break;
                    case SYNC_DATE:
                        Long syncDate = GsonRedisUtils.fromBytes(item.getValue(), Long.class);
                        builder.setSyncDate(syncDate);
                        break;
                    case QUARTER:
                        String quarter = GsonRedisUtils.fromBytes(item.getValue(), String.class);
                        builder.setQuarter(quarter);
                        break;
                    case REPLY_FLAG_CUSTOMER:
                        Integer replyFlagCustomer = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setReplyFlagCustomer(replyFlagCustomer);
                        break;
                    case REPLY_FLAG_KEFU:
                        Integer replyFlagKefu = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setReplyFlagKefu(replyFlagKefu);
                        break;
                    case REPLY_FLAG:
                        Integer replyFlag = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setReplyFlag(replyFlag);
                        break;
                    case FEEDBACK_DATE:
                        Date feedbackDate = GsonRedisUtils.fromBytes(item.getValue(), Date.class);
                        builder.setFeedbackDate(feedbackDate);
                        break;
                    case FEEDBACK_TITLE:
                        String feedbackTitle = GsonRedisUtils.fromBytes(item.getValue(), String.class);
                        builder.setFeedbackTitle(feedbackTitle);
                        break;
                    case FEEDBACK_FLAG:
                        Integer feedbackFlag = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setFeedbackFlag(feedbackFlag);
                        break;
                    case FINISH_PHOTO_QTY:
                        Long finishPhotoQty = GsonRedisUtils.fromBytes(item.getValue(), Long.class);
                        builder.setFinishPhotoQty(finishPhotoQty);
                        break;
                    case PENDING_TYPE:
                        Dict pendingType = GsonRedisUtils.fromBytes(item.getValue(), Dict.class);
                        builder.setPendingType(pendingType);
                        break;
                    case ATTACHMENTS:
                        OrderAttachment[] attachments = GsonRedisUtils.fromBytes(item.getValue(), OrderAttachment[].class);
                        if (attachments != null) {
                            builder.setAttachments(Arrays.asList(attachments));
                        }
                        break;
                    case INFO:
                        Order info = OrderRedisAdapter.getInstance().fromJson(StringUtils2.fromGsonString(new String(item.getValue()).replace("\\\\\\\"", "").replace("\\r\\n", "<br>")));
                        builder.setInfo(info);
                        break;
                    case ORDER_STATUS:
                        OrderStatus orderStatus = GsonRedisUtils.fromBytes(item.getValue(), OrderStatus.class);
                        builder.setOrderStatus(orderStatus);
                        break;
                    case CONDITION:
                        OrderCondition condition = OrderConditionRedisAdapter.getInstance().fromJson(GsonRedisUtils.fromBytes(item.getValue(), String.class));
                        builder.setCondition(condition);
                        break;
                    case ORDER_ITEM_COMPLETES:
                        OrderItemComplete[] orderItemCompletes = GsonRedisUtils.fromBytes(item.getValue(), OrderItemComplete[].class);
                        if (orderItemCompletes != null) {
                            builder.setOrderItemCompletes(Arrays.asList(orderItemCompletes));
                        }
                        break;
                    case PENDING_FLAG:
                        Integer pendingFlag = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setPendingFlag(pendingFlag);
                        break;
                    case STATUS:
                        Dict status = GsonRedisUtils.fromBytes(item.getValue(), Dict.class);
                        builder.setStatus(status);
                        break;
                    case FEE:
                        OrderFee fee = GsonRedisUtils.fromBytes(item.getValue(), OrderFee.class);
                        builder.setFee(fee);
                        break;
                    case PARTS_FLAG:
                        Integer partsFlag = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setPartsFlag(partsFlag);
                        break;
                    case TRACKING_FLAG:
                        Integer trackingFlag = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setTrackingFlag(trackingFlag);
                        break;
                    case TRACKING_DATE:
                        Date trackingDate = GsonRedisUtils.fromBytes(item.getValue(), Date.class);
                        builder.setTrackingDate(trackingDate);
                        break;
                    case TRACKING_MESSAGE:
                        String trackingMessage = GsonRedisUtils.fromBytes(item.getValue(), String.class);
                        builder.setTrackingMessage(trackingMessage);
                        break;
                    case PENDING_TYPE_DATE:
                        Date pendingTypeDate = GsonRedisUtils.fromBytes(item.getValue(), Date.class);
                        builder.setPendingTypeDate(pendingTypeDate);
                        break;
                    case SERVICE_TIMES:
                        Integer serviceTimes = GsonRedisUtils.fromBytes(item.getValue(), Integer.class);
                        builder.setServiceTimes(serviceTimes);
                        break;
                }
            }
        }
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private Map<String, byte[]> getAllFromRedis(final String orderCacheKey) {
        final byte[] bKey = orderCacheKey.getBytes(StandardCharsets.UTF_8);
        Map<String, byte[]> result = Maps.newHashMap();
        Map<byte[], byte[]> map;
        try {
            map = (Map<byte[], byte[]>) redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_SD_DB.ordinal());
                return connection.hGetAll(bKey);
            });
            if (map != null && !map.isEmpty()) {
                String field;
                for (Map.Entry<byte[], byte[]> item : map.entrySet()) {
                    if (item.getKey() != null && item.getValue() != null) {
                        field = StringUtils.toString(item.getKey());
                        result.put(field, item.getValue());
                    }
                }
            }
        } catch (Exception e) {
            //LogUtils.saveLog("OrderCacheUtils", "getAllFromRedis", orderCacheKey, e, null);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<String, byte[]> getPartialFromRedis(final String orderCacheKey, List<String> fieldNames) {
        final byte[] bKey = orderCacheKey.getBytes(StandardCharsets.UTF_8);
        int size = fieldNames.size();
        byte[][] bFieldNames = new byte[size][];
        for (int i = 0; i < size; i++) {
            bFieldNames[i] = fieldNames.get(i).getBytes(StandardCharsets.UTF_8);
        }
        Map<String, byte[]> result = Maps.newHashMap();
        List<byte[]> list;
        try {
            list = (List<byte[]>) redisTemplate.execute((RedisCallback<List<byte[]>>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_SD_DB.ordinal());
                return connection.hMGet(bKey, bFieldNames);
            });
            if (list != null && !list.isEmpty() && list.size() == size) {
                byte[] value;
                for (int i = 0; i < list.size(); i++) {
                    value = list.get(i);
                    if (value != null) {
                        result.put(fieldNames.get(i), value);
                    }
                }
            }
        } catch (Exception e) {
            //LogUtils.saveLog("OrderCacheUtils", "getFieldsFromRedis", orderCacheKey, e, null);
        }
        return result;


    }

    /**
     * 判断缓存中是否有上门服务变更标记
     * @param orderId   订单id
     * @return Boolean
     */
    public boolean hasSetDetailActionFlag(Long orderId){
        if(orderId == null || orderId <= 0){
            return false;
        }
        boolean result = false;
        try {
            final byte[] bKey = String.format(RedisConstant.SD_ORDER_DETAIL_FLAG,orderId).getBytes(StandardCharsets.UTF_8);
            result = (Boolean)redisTemplate.execute((RedisCallback<Object>) connection -> {
                connection.select(RedisConstant.RedisDBType.REDIS_LOCK_DB.ordinal());
                return connection.exists(bKey);
            });
        } catch (Exception e) {
            result = false;
            //LogUtils.saveLog("OrderCacheUtils", "setDetailActionFlag", orderId.toString(), e, null);
        }
        return result;
    }

    //endregion 读取
}
