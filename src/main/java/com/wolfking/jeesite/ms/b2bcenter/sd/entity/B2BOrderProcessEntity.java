package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderProcessMessage;
import com.kkl.kklplus.entity.b2bcenter.sd.B2BOrderActionEnum;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.Getter;

import java.io.Serializable;

public class B2BOrderProcessEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Getter
    private Long messageId;
    @Getter
    private Long kklOrderId;
    @Getter
    private Long b2bOrderId;
    @Getter
    private String b2bOrderNo;
    @Getter
    private Integer dataSourceId;

    @Getter
    private B2BOrderActionEnum actionType;

    @Getter
    private Integer status;
    @Getter
    private String remarks;

    private B2BOrderProcessEntity(Builder builder) {
        this.messageId = builder.messageId;
        this.kklOrderId = builder.kklOrderId;
        this.b2bOrderId = builder.b2bOrderId;
        this.b2bOrderNo = builder.b2bOrderNo;
        this.dataSourceId = builder.dataSourceId;
        this.actionType = builder.actionType;
        this.status = builder.status;
        this.remarks = builder.remarks;
    }

    public static B2BOrderProcessEntity toB2BOrderProcessEntity(MQB2BOrderProcessMessage.B2BOrderProcessMessage message) {
        B2BOrderProcessEntity entity = null;
        if (message != null) {
            Builder builder = new Builder();
            builder.setMessageId(message.getMessageId())
                    .setKklOrderId(message.getKklOrderId())
                    .setB2bOrderId(message.getB2BOrderId())
                    .setB2bOrderNo(message.getB2BOrderNo())
                    .setDataSourceId(message.getDataSource())
                    .setActionType(message.getActionType())
                    .setStatus(message.getStatus())
                    .setRemarks(message.getRemarks());
            entity = builder.build();
        }
        return entity;
    }

    public static void saveFailureLog(B2BOrderProcessEntity processEntity, String title, String methodName, Exception ex) {
        String logJson = GsonUtils.toGsonString(processEntity);
        LogUtils.saveLog(title, methodName, logJson, ex, null);
    }

    /**
     * Builder实体构造工具类
     */
    public static class Builder {
        private Long messageId = 0L;
        private Long kklOrderId = 0L;
        private Long b2bOrderId = 0L;
        private String b2bOrderNo = "";
        private Integer dataSourceId = 0;
        private B2BOrderActionEnum actionType = B2BOrderActionEnum.NONE;
        private Integer status = 0;
        private String remarks = "";

        public Builder setMessageId(Long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setKklOrderId(Long kklOrderId) {
            this.kklOrderId = kklOrderId;
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

        public Builder setDataSourceId(Integer dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }

        public Builder setActionType(Integer actionType) {
            this.actionType = B2BOrderActionEnum.valueOf(actionType);
            return this;
        }

        public Builder setStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder setRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public B2BOrderProcessEntity build() {
            return new B2BOrderProcessEntity(this);
        }
    }

}
