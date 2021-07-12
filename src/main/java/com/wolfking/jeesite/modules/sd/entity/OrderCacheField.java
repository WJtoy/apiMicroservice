package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Maps;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.util.Date;
import java.util.Map;

/**
 * 工单缓存的字段类型
 */
public enum OrderCacheField {

    VERSION(1, "version", Long.class),
    APPOINTMENT_DATE(2, "appointmentDate", Date.class),
    RESERVATION_DATE(3, "reservationDate", Date.class),
    RESERVATION_TIMES(4, "reservationTimes", Integer.class),
    WRITE_TIME(5, "wirteTime", String.class),
    SYNC_DATE(6, "syncDate", Long.class),
    QUARTER(7, "quarter", String.class),
    REPLY_FLAG_CUSTOMER(8, "replyFlagCustomer", Integer.class),
    REPLY_FLAG_KEFU(10, "replyFlagKefu", Integer.class),
    REPLY_FLAG(11, "replyFlag", Integer.class),
    FEEDBACK_DATE(12, "feedbackDate", Date.class),
    FEEDBACK_TITLE(13, "feedbackTitle", String.class),
    FEEDBACK_FLAG(14, "feedbackFlag", Integer.class),
    FINISH_PHOTO_QTY(15, "finishPhotoQty", Long.class),

    PENDING_TYPE(16, "pendingType", Dict.class),
    ATTACHMENTS(17, "attachments", OrderAttachment[].class),  //List<OrderAttachment>
    INFO(18, "info", Order.class),
    ORDER_STATUS(19, "orderStatus", OrderStatus.class),
    CONDITION(20, "condition", OrderCondition.class),

    ORDER_ITEM_COMPLETES(21, "orderItemCompletes", OrderItemComplete[].class),
    PENDING_FLAG(22, "pendingFlag", Integer.class),
    STATUS(23, "status", Dict.class),
    FEE(24, "fee", OrderFee.class),
    PARTS_FLAG(25, "partsFlag", Integer.class),

    TRACKING_FLAG(26, "trackingFlag", Integer.class),
    TRACKING_DATE(27, "trackingDate", Date.class),
    TRACKING_MESSAGE(28, "trackingMessage", String.class),

    PENDING_TYPE_DATE(29, "pendingTypeDate", Date.class),
    SERVICE_TIMES(30, "serviceTimes", Integer.class);

    private int id;
    private String name;
    private Class<?> clazz;

    OrderCacheField(int id, String name, Class<?> clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    private static final Map<String, OrderCacheField> MAP = Maps.newHashMap();

    static {
        for (OrderCacheField field : OrderCacheField.values()) {
            MAP.put(field.name, field);
        }
    }

    public static OrderCacheField get(String name) {
        OrderCacheField field = null;
        if (StringUtils.isNotBlank(name)) {
            field = MAP.get(name);
        }
        return field;
    }
}
