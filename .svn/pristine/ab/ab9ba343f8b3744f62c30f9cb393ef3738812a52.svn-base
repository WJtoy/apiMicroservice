package com.wolfking.jeesite.modules.sd.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import lombok.Getter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用于工单缓存的辅助实体类
 */
public class OrderCacheParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private String orderCacheKey;

    @Getter
    private OrderCacheOpType opType;

    @Getter
    private Map<OrderCacheField, Object> updateParams;

    @Getter
    private List<OrderCacheField> deleteParams;

    @Getter
    private Map<OrderCacheField, Long> incrementParams;

    @Getter
    private Long expireSeconds;

    private OrderCacheParam(Builder builder) {
        this.orderCacheKey = builder.orderCacheKey;
        this.opType = builder.opType;
        this.updateParams = builder.updateParams;
        this.deleteParams = builder.deleteParams;
        this.incrementParams = builder.incrementParams;
        this.expireSeconds = builder.expireSeconds;
    }

    /**
     * Builder实体构造工具类
     */
    public static class Builder {
        private String orderCacheKey;
        private OrderCacheOpType opType = OrderCacheOpType.UPDATE;
        private Map<OrderCacheField, Object> updateParams = Maps.newHashMap();
        private List<OrderCacheField> deleteParams = Lists.newArrayList();
        private Map<OrderCacheField, Long> incrementParams = Maps.newHashMap();
        private Long expireSeconds = 0L;

        public Builder setOrderId(Long orderId) {
            if (orderId != null && orderId > 0) {
                this.orderCacheKey = String.format(RedisConstant.SD_ORDER, orderId);
            }
            return this;
        }

        public Builder setOpType(OrderCacheOpType opType) {
            if (opType != null) {
                this.opType = opType;
            }
            return this;
        }

        /**
         * 在原有的版本号基本上增加
         */
        public Builder incrVersion(Long version) {
            if (version != null && version != 0) {
                incrementParams.put(OrderCacheField.VERSION, version);
            }
            return this;
        }

        /**
         * 更新版本号
         */
        public Builder setVersion(Long version) {
            if (version != null) {
                updateParams.put(OrderCacheField.VERSION, version);
            }
            return this;
        }

        public Builder setAppointmentDate(Date appointmentDate) {
            if (appointmentDate != null) {
                updateParams.put(OrderCacheField.APPOINTMENT_DATE, appointmentDate);
            } else {
                deleteParams.add(OrderCacheField.APPOINTMENT_DATE);
            }
            return this;
        }

        public Builder setReservationDate(Date reservationDate) {
            if (reservationDate != null) {
                updateParams.put(OrderCacheField.RESERVATION_DATE, reservationDate);
            } else {
                deleteParams.add(OrderCacheField.RESERVATION_DATE);
            }
            return this;
        }

        public Builder setReservationTimes(Integer reservationTimes) {
            if (reservationTimes != null) {
                updateParams.put(OrderCacheField.RESERVATION_TIMES, reservationTimes);
            } else {
                deleteParams.add(OrderCacheField.RESERVATION_TIMES);
            }
            return this;
        }

        public Builder setWriteTime(String writeTime) {
            if (writeTime != null) {
                updateParams.put(OrderCacheField.WRITE_TIME, writeTime);
            }
            return this;
        }

        public Builder setSyncDate(Long syncDate) {
            if (syncDate != null) {
                updateParams.put(OrderCacheField.SYNC_DATE, syncDate);
            }
            return this;
        }

        public Builder setQuarter(String quarter) {
            if (StringUtils.isNotBlank(quarter)) {
                updateParams.put(OrderCacheField.QUARTER, quarter);
            } else {
                deleteParams.add(OrderCacheField.QUARTER);
            }
            return this;
        }

        public Builder setReplyFlagCustomer(Integer replyFlagCustomer) {
            if (replyFlagCustomer != null) {
                updateParams.put(OrderCacheField.REPLY_FLAG_CUSTOMER, replyFlagCustomer);
            } else {
                deleteParams.add(OrderCacheField.REPLY_FLAG_CUSTOMER);
            }
            return this;
        }

        public Builder setReplyFlagKefu(Integer replyFlagKefu) {
            if (replyFlagKefu != null) {
                updateParams.put(OrderCacheField.REPLY_FLAG_KEFU, replyFlagKefu);
            } else {
                deleteParams.add(OrderCacheField.REPLY_FLAG_KEFU);
            }
            return this;
        }

        public Builder setReplyFlag(Integer replyFlag) {
            if (replyFlag != null) {
                updateParams.put(OrderCacheField.REPLY_FLAG, replyFlag);
            } else {
                deleteParams.add(OrderCacheField.REPLY_FLAG);
            }
            return this;
        }

        public Builder setFeedbackDate(Date feedbackDate) {
            if (feedbackDate != null) {
                updateParams.put(OrderCacheField.FEEDBACK_DATE, feedbackDate);
            } else {
                deleteParams.add(OrderCacheField.FEEDBACK_DATE);
            }
            return this;
        }

        public Builder setFeedbackTitle(String feedbackTitle) {
            if (feedbackTitle != null) {
                updateParams.put(OrderCacheField.FEEDBACK_TITLE, feedbackTitle);
            } else {
                deleteParams.add(OrderCacheField.FEEDBACK_TITLE);
            }
            return this;
        }

        public Builder setFeedbackFlag(Integer feedbackFlag) {
            if (feedbackFlag != null) {
                updateParams.put(OrderCacheField.FEEDBACK_FLAG, feedbackFlag);
            } else {
                deleteParams.add(OrderCacheField.FEEDBACK_FLAG);
            }
            return this;
        }

        /**
         * 在原有的图片数量基础上增加
         */
        public Builder incrFinishPhotoQty(Long finishPhotoQty) {
            if (finishPhotoQty != null && finishPhotoQty != 0) {
                incrementParams.put(OrderCacheField.FINISH_PHOTO_QTY, finishPhotoQty);
            }
            return this;
        }

        /**
         * 更新图片数量
         */
        public Builder setFinishPhotoQty(Long finishPhotoQty) {
            if (finishPhotoQty != null) {
                updateParams.put(OrderCacheField.FINISH_PHOTO_QTY, finishPhotoQty);
            } else {
                deleteParams.add(OrderCacheField.FINISH_PHOTO_QTY);
            }
            return this;
        }

        public Builder setPendingType(Dict pendingType) {
            if (pendingType != null) {
                updateParams.put(OrderCacheField.PENDING_TYPE, pendingType);
            } else {
                deleteParams.add(OrderCacheField.PENDING_TYPE);
            }
            return this;
        }

        public Builder setPendingTypeDate(Date pendingTypeDate) {
            if (pendingTypeDate != null) {
                updateParams.put(OrderCacheField.PENDING_TYPE_DATE, pendingTypeDate);
            } else {
                deleteParams.add(OrderCacheField.PENDING_TYPE_DATE);
            }
            return this;
        }

        public Builder setAttachments(List<OrderAttachment> attachments) {
            if (attachments != null && !attachments.isEmpty()) {
                updateParams.put(OrderCacheField.ATTACHMENTS, attachments);
            } else {
                deleteParams.add(OrderCacheField.ATTACHMENTS);
            }
            return this;
        }

        public Builder setInfo(Order info) {
            if (info != null) {
                updateParams.put(OrderCacheField.INFO, info);
            } else {
                deleteParams.add(OrderCacheField.INFO);
            }
            return this;
        }

        public Builder setOrderStatus(OrderStatus orderStatus) {
            if (orderStatus != null) {
                updateParams.put(OrderCacheField.ORDER_STATUS, orderStatus);
            } else {
                deleteParams.add(OrderCacheField.ORDER_STATUS);
            }
            return this;
        }

        public Builder setCondition(OrderCondition condition) {
            if (condition != null) {
                updateParams.put(OrderCacheField.CONDITION, condition);
            } else {
                deleteParams.add(OrderCacheField.CONDITION);
            }
            return this;
        }

        public Builder setPendingFlag(Integer pendingFlag) {
            if (pendingFlag != null) {
                updateParams.put(OrderCacheField.PENDING_FLAG, pendingFlag);
            } else {
                deleteParams.add(OrderCacheField.PENDING_FLAG);
            }
            return this;
        }

        public Builder setStatus(Dict status) {
            if (status != null) {
                updateParams.put(OrderCacheField.STATUS, status);
            } else {
                deleteParams.add(OrderCacheField.STATUS);
            }
            return this;
        }

        public Builder setFee(OrderFee fee) {
            if (fee != null) {
                updateParams.put(OrderCacheField.FEE, fee);
            } else {
                deleteParams.add(OrderCacheField.FEE);
            }
            return this;
        }

        public Builder setPartsFlag(Integer partsFlag) {
            if (partsFlag != null) {
                updateParams.put(OrderCacheField.PARTS_FLAG, partsFlag);
            } else {
                deleteParams.add(OrderCacheField.PARTS_FLAG);
            }
            return this;
        }

        public Builder setTrackingFlag(Integer trackingFlag) {
            if (trackingFlag != null) {
                updateParams.put(OrderCacheField.TRACKING_FLAG, trackingFlag);
            } else {
                deleteParams.add(OrderCacheField.TRACKING_FLAG);
            }
            return this;
        }

        public Builder setTrackingDate(Date trackingDate) {
            updateParams.put(OrderCacheField.TRACKING_DATE, trackingDate);
            return this;
        }

        public Builder setTrackingMessage(String trackingMessage) {
            if (trackingMessage != null) {
                updateParams.put(OrderCacheField.TRACKING_MESSAGE, trackingMessage);
            } else {
                deleteParams.add(OrderCacheField.TRACKING_MESSAGE);
            }
            return this;
        }

        public Builder setDeleteField(OrderCacheField field) {
            if (field != null) {
                deleteParams.add(field);
            }
            return this;
        }

        public Builder setDeleteFields(List<OrderCacheField> fields) {
            if (fields != null && !fields.isEmpty()) {
                deleteParams.addAll(fields);
            }
            return this;
        }

        /**
         * expireSeconds的默认值为0
         */
        public Builder setExpireSeconds(Long expireSeconds) {
            if (expireSeconds != null) {
                this.expireSeconds = expireSeconds;
            }
            return this;
        }

        public OrderCacheParam build() {
            Assert.isTrue(StringUtils.isNotBlank(orderCacheKey), "工单缓存的KEY不能为空");
            return new OrderCacheParam(this);
        }
    }
}
