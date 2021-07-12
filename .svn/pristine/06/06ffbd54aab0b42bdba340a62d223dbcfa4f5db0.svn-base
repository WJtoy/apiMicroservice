package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class B2BAsyncTriggerB2BOperationReqEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 异步操作
     */
    private Integer b2bAsyncOperationId;
    /**
     * 数据源id
     */
    private Integer dataSourceId;
    /**
     * 快可立工单id
     */
    private Long kklOrderId;
    /**
     * 快可立工单分片
     */
    private String kklQuarter;
    /**
     * B2B工单ID
     */
    private Long b2bOrderId;
    /**
     * B2B单号
     */
    private String b2bOrderNo;
    /**
     * 操作人ID
     */
    private Long updaterId;
    /**
     * 操作时间
     */
    private Long updateDt;

    private B2BAsyncTriggerB2BOperationReqEntity(Builder builder) {
        this.b2bAsyncOperationId = builder.b2bAsyncOperationId;
        this.dataSourceId = builder.dataSourceId;
        this.kklOrderId = builder.kklOrderId;
        this.kklQuarter = builder.kklQuarter;
        this.b2bOrderId = builder.b2bOrderId;
        this.b2bOrderNo = builder.b2bOrderNo;
        this.updaterId = builder.updaterId;
        this.updateDt = builder.updateDt;
    }

    /**
     * Builder实体构造工具类
     */
    public static class Builder {
        private Integer b2bAsyncOperationId = 0;
        private Integer dataSourceId = 0;
        private Long kklOrderId = 0L;
        private String kklQuarter = "";
        private Long b2bOrderId = 0L;
        private String b2bOrderNo = "";
        private Long updaterId = 0L;
        private Long updateDt = 0L;

        public Builder setB2bAsyncOperationId(Integer b2bAsyncOperationId) {
            this.b2bAsyncOperationId = b2bAsyncOperationId;
            return this;
        }

        public Builder setDataSourceId(Integer dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }

        public Builder setKklOrderId(Long kklOrderId) {
            this.kklOrderId = kklOrderId;
            return this;
        }

        public Builder setKklQuarter(String kklQuarter) {
            this.kklQuarter = kklQuarter;
            return this;
        }

        public Builder setB2bOrderId(Long b2bOrderId) {
            this.b2bOrderId = b2bOrderId;
            return this;
        }

        public Builder setB2bOrderNo(String b2bOrderNo) {
            this.b2bOrderNo = b2bOrderNo;
            return this;
        }

        public Builder setUpdaterId(Long updaterId) {
            this.updaterId = updaterId;
            return this;
        }

        public Builder setUpdateDt(Long updateDt) {
            this.updateDt = updateDt;
            return this;
        }

        public B2BAsyncTriggerB2BOperationReqEntity build() {
            return new B2BAsyncTriggerB2BOperationReqEntity(this);
        }
    }

}
