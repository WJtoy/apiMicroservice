package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class B2BOrderProcessLogReqEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    private Long id = 0L;

    /**
     * 快可立工单id
     */
    private Long orderId = 0L;

    /**
     * 数据源id
     */
    private Integer dataSourceId = 0;

    /**
     * 工单客户ID
     */
    private Long customerId = 0L;

    /**
     * 操作人名称
     */
    private String operatorName = "";


    /**
     * 日志时间（时间戳）
     */
    private Long logDt = 0L;

    /**
     * 日志类型
     */
    private String logType = "";

    /**
     * 日志标题
     */
    private String logTitle = "";

    /**
     * 日志内容
     */
    private String logContext = "";

    /**
     * 日志备注
     */
    private String logRemarks = "";

    /**
     * 创建者id
     */
    private Long createById = 0L;

    /**
     * 创建时间（时间戳）
     */
    private Long createDt = 0L;

    private B2BOrderProcessLogReqEntity(Builder builder) {
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.dataSourceId = builder.dataSourceId;
        this.customerId = builder.customerId;
        this.operatorName = builder.operatorName;
        this.logDt = builder.logDt;
        this.logType = builder.logType;
        this.logTitle = builder.logTitle;
        this.logContext = builder.logContext;
        this.logRemarks = builder.logRemarks;
        this.createById = builder.createById;
        this.createDt = builder.createDt;
    }

    /**
     * Builder实体构造工具类
     */
    public static class Builder {
        private Long id = 0L;
        private Long orderId = 0L;
        private Integer dataSourceId = 0;
        private Long customerId = 0L;
        private String operatorName = "";
        private Long logDt = 0L;
        private String logType = "";
        private String logTitle = "";
        private String logContext = "";
        private String logRemarks = "";
        private Long createById = 0L;
        private Long createDt = 0L;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder setDataSourceId(Integer dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }

        public Builder setCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder setOperatorName(String operatorName) {
            this.operatorName = operatorName;
            return this;
        }

        public Builder setLogDt(Long logDt) {
            this.logDt = logDt;
            return this;
        }

        public Builder setLogType(String logType) {
            this.logType = logType;
            return this;
        }

        public Builder setLogTitle(String logTitle) {
            this.logTitle = logTitle;
            return this;
        }

        public Builder setLogContext(String logContext) {
            this.logContext = logContext;
            return this;
        }

        public Builder setLogRemarks(String logRemarks) {
            this.logRemarks = logRemarks;
            return this;
        }

        public Builder setCreateById(Long createById) {
            this.createById = createById;
            return this;
        }

        public Builder setCreateDt(Long createDt) {
            this.createDt = createDt;
            return this;
        }

        public B2BOrderProcessLogReqEntity build() {
            return new B2BOrderProcessLogReqEntity(this);
        }
    }

}
