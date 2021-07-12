package com.wolfking.jeesite.modules.sd.entity;

import com.wolfking.jeesite.modules.sys.entity.Dict;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderCacheResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private Long version;
    @Getter
    private Date appointmentDate;
    @Getter
    private Date reservationDate;
    @Getter
    private Integer reservationTimes;
    @Getter
    private String writeTime;
    @Getter
    private Long syncDate;
    @Getter
    private String quarter;
    @Getter
    private Integer replyFlagCustomer;
    @Getter
    private Integer replyFlagKefu;
    @Getter
    private Integer replyFlag;
    @Getter
    private Date feedbackDate;
    @Getter
    private String feedbackTitle;
    @Getter
    private Integer feedbackFlag;
    @Getter
    private Long finishPhotoQty;
    @Getter
    private Dict pendingType;
    @Getter
    private List<OrderAttachment> attachments;
    @Getter
    private Order info;
    @Getter
    private OrderStatus orderStatus;
    @Getter
    private OrderCondition condition;
    @Getter
    private Integer pendingFlag;
    @Getter
    private Dict status;
    @Getter
    private OrderFee fee;
    @Getter
    private Integer partsFlag;
    @Getter
    private Integer trackingFlag;
    @Getter
    private Date trackingDate;
    @Getter
    private String trackingMessage;

    @Getter
    private Date pendingTypeDate;
    @Getter
    private Integer serviceTimes;
    @Getter
    private List<OrderItemComplete> orderItemCompletes;

    private OrderCacheResult(Builder builder) {
        this.version = builder.version;
        this.appointmentDate = builder.appointmentDate;
        this.reservationDate = builder.reservationDate;
        this.reservationTimes = builder.reservationTimes;
        this.writeTime = builder.writeTime;
        this.syncDate = builder.syncDate;
        this.quarter = builder.quarter;
        this.replyFlagCustomer = builder.replyFlagCustomer;
        this.replyFlagKefu = builder.replyFlagKefu;
        this.replyFlag = builder.replyFlag;
        this.feedbackDate = builder.feedbackDate;
        this.feedbackTitle = builder.feedbackTitle;
        this.feedbackFlag = builder.feedbackFlag;
        this.finishPhotoQty = builder.finishPhotoQty;
        this.pendingType = builder.pendingType;
        this.attachments = builder.attachments;
        this.info = builder.info;
        this.orderStatus = builder.orderStatus;
        this.condition = builder.condition;
        this.pendingFlag = builder.pendingFlag;
        this.status = builder.status;
        this.fee = builder.fee;
        this.partsFlag = builder.partsFlag;
        this.trackingFlag = builder.trackingFlag;
        this.trackingDate = builder.trackingDate;
        this.trackingMessage = builder.trackingMessage;

        this.pendingTypeDate = builder.pendingTypeDate;
        this.serviceTimes = builder.serviceTimes;
        this.orderItemCompletes = builder.orderItemCompletes;
    }

    public static class Builder {
        private Long version;
        private Date appointmentDate;
        private Date reservationDate;
        private Integer reservationTimes;
        private String writeTime;
        private Long syncDate;
        private String quarter;
        private Integer replyFlagCustomer;
        private Integer replyFlagKefu;
        private Integer replyFlag;
        private Date feedbackDate;
        private String feedbackTitle;
        private Integer feedbackFlag;
        private Long finishPhotoQty;
        private Dict pendingType;
        private List<OrderAttachment> attachments;
        private Order info;
        private OrderStatus orderStatus;
        private OrderCondition condition;
        private Integer pendingFlag;
        private Dict status;
        private OrderFee fee;
        private Integer partsFlag;
        private Integer trackingFlag;
        private Date trackingDate;
        private String trackingMessage;

        private Date pendingTypeDate;
        private Integer serviceTimes;
        private List<OrderItemComplete> orderItemCompletes;

        public Builder setVersion(Long version) {
            this.version = version;
            return this;
        }

        public Builder setAppointmentDate(Date appointmentDate) {
            this.appointmentDate = appointmentDate;
            return this;
        }

        public Builder setReservationDate(Date reservationDate) {
            this.reservationDate = reservationDate;
            return this;
        }

        public Builder setReservationTimes(Integer reservationTimes) {
            this.reservationTimes = reservationTimes;
            return this;
        }

        public Builder setWriteTime(String writeTime) {
            this.writeTime = writeTime;
            return this;
        }

        public Builder setSyncDate(Long syncDate) {
            this.syncDate = syncDate;
            return this;
        }

        public Builder setQuarter(String quarter) {
            this.quarter = quarter;
            return this;
        }

        public Builder setReplyFlagCustomer(Integer replyFlagCustomer) {
            this.replyFlagCustomer = replyFlagCustomer;
            return this;
        }

        public Builder setReplyFlagKefu(Integer replyFlagKefu) {
            this.replyFlagKefu = replyFlagKefu;
            return this;
        }

        public Builder setReplyFlag(Integer replyFlag) {
            this.replyFlag = replyFlag;
            return this;
        }

        public Builder setFeedbackDate(Date feedbackDate) {
            this.feedbackDate = feedbackDate;
            return this;
        }

        public Builder setFeedbackTitle(String feedbackTitle) {
            this.feedbackTitle = feedbackTitle;
            return this;
        }

        public Builder setFeedbackFlag(Integer feedbackFlag) {
            this.feedbackFlag = feedbackFlag;
            return this;
        }

        public Builder setFinishPhotoQty(Long finishPhotoQty) {
            this.finishPhotoQty = finishPhotoQty;
            return this;
        }

        public Builder setPendingType(Dict pendingType) {
            this.pendingType = pendingType;
            return this;
        }

        public Builder setAttachments(List<OrderAttachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public Builder setInfo(Order info) {
            this.info = info;
            return this;
        }

        public Builder setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setCondition(OrderCondition condition) {
            this.condition = condition;
            return this;
        }

        public Builder setPendingFlag(Integer pendingFlag) {
            this.pendingFlag = pendingFlag;
            return this;
        }

        public Builder setStatus(Dict status) {
            this.status = status;
            return this;
        }

        public Builder setFee(OrderFee fee) {
            this.fee = fee;
            return this;
        }

        public Builder setPartsFlag(Integer partsFlag) {
            this.partsFlag = partsFlag;
            return this;
        }

        public Builder setTrackingFlag(Integer trackingFlag) {
            this.trackingFlag = trackingFlag;
            return this;
        }

        public Builder setTrackingDate(Date trackingDate) {
            this.trackingDate = trackingDate;
            return this;
        }

        public Builder setTrackingMessage(String trackingMessage) {
            this.trackingMessage = trackingMessage;
            return this;
        }

        public Builder setPendingTypeDate(Date pendingTypeDate) {
            this.pendingTypeDate = pendingTypeDate;
            return this;
        }

        public Builder setServiceTimes(Integer serviceTimes) {
            this.serviceTimes = serviceTimes;
            return this;
        }

        public Builder setOrderItemCompletes(List<OrderItemComplete> orderItemCompletes) {
            this.orderItemCompletes = orderItemCompletes;
            return this;
        }

        public OrderCacheResult build() {
            return new OrderCacheResult(this);
        }
    }

}
