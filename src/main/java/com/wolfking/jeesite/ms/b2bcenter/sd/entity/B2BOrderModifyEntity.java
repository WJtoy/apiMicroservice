package com.wolfking.jeesite.ms.b2bcenter.sd.entity;

import com.kkl.kklplus.entity.b2bcenter.md.B2BModifyOperationEnum;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderModifyMessage;
import com.wolfking.jeesite.common.utils.GsonUtils;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.Getter;

import java.io.Serializable;

public class B2BOrderModifyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 快可立工单id
     */
    @Getter
    private Long kklOrderId;

    @Getter
    private String b2bOrderNo;

    /**
     * 数据源id
     */
    @Getter
    private Integer dataSourceId;

    /**
     * 操作类型
     */
    @Getter
    private B2BModifyOperationEnum operationType;

    /**
     * //修改时间
     */
    @Getter
    private Long changeTime;

    /**
     * 送货时间
     */
    @Getter
    private Long hopeArrivalTime;

    /**
     * 操作时间
     */
    @Getter
    private Long operateTime;

    /**
     * 服务时间
     */
    @Getter
    private Long serviceTime;

    /**
     * 买家姓名
     */
    @Getter
    private String userName;

    /**
     * 用户手机
     */
    @Getter
    private String userMobile;

    /**
     * 用户电话
     */
    @Getter
    private String userPhone;

    /**
     * 地址
     */
    @Getter
    private String userAddress;

    /**
     * 省
     */
    @Getter
    private String userProvince;

    /**
     * 市
     */
    @Getter
    private String userCity;

    /**
     * 区
     */
    @Getter
    private String userCounty;

    /**
     * 街道
     */
    @Getter
    private String userStreet;

    /**
     * 备注
     */
    @Getter
    private String remarks;

    /**
     * 快递公司
     */
    @Getter
    private String expressCompany;

    /**
     * 快递单号
     */
    @Getter
    private String expressNo;

    /**
     * 发货时间
     */
    @Getter
    private Long deliveryTime;

    private B2BOrderModifyEntity(Builder builder) {
        this.kklOrderId = builder.kklOrderId;
        this.b2bOrderNo = builder.b2bOrderNo;
        this.dataSourceId = builder.dataSourceId;
        this.operationType = builder.operationType;
        this.changeTime = builder.changeTime;
        this.hopeArrivalTime = builder.hopeArrivalTime;
        this.operateTime = builder.operateTime;
        this.serviceTime = builder.serviceTime;
        this.userName = builder.userName;
        this.userMobile = builder.userMobile;
        this.userPhone = builder.userPhone;
        this.userAddress = builder.userAddress;
        this.userProvince = builder.userProvince;
        this.userCity = builder.userCity;
        this.userCounty = builder.userCounty;
        this.userStreet = builder.userStreet;
        this.remarks = builder.remarks;
        this.expressCompany = builder.expressCompany;
        this.expressNo = builder.expressNo;
        this.deliveryTime = builder.deliveryTime;
    }

    public static B2BOrderModifyEntity toB2BOrderModifyEntity(MQB2BOrderModifyMessage.B2BOrderModifyMessage message) {
        B2BOrderModifyEntity entity = null;
        if (message != null) {
            Builder builder = new Builder();
            builder.setKklOrderId(message.getKklOrderId())
                    .setB2bOrderNo(message.getB2BOrderNo())
                    .setDataSourceId(message.getDataSource())
                    .setOperationType(message.getOperationType())
                    .setChangeTime(message.getChangeTime())
                    .setHopeArrivalTime(message.getHopeArrivalTime())
                    .setOperateTime(message.getOperateTime())
                    .setServiceTime(message.getServiceTime())
                    .setUserName(message.getUserName())
                    .setUserMobile(message.getUserMobile())
                    .setUserPhone(message.getUserPhone())
                    .setUserAddress(message.getUserAddress())
                    .setUserProvince(message.getUserProvince())
                    .setUserCity(message.getUserCity())
                    .setUserCounty(message.getUserCounty())
                    .setUserStreet(message.getUserStreet())
                    .setRemarks(message.getRemarks())
                    .setExpressCompany(message.getExpressCompany())
                    .setExpressNo(message.getExpressNo())
                    .setDeliveryTime(message.getDeliveryTime());
            entity = builder.build();
        }
        return entity;
    }

    public static void saveFailureLog(B2BOrderModifyEntity modifyEntity, String title, String methodName, Exception ex) {
        String logJson = GsonUtils.toGsonString(modifyEntity);
        LogUtils.saveLog(title, methodName, logJson, ex, null);
    }

    /**
     * Builder实体构造工具类
     */
    public static class Builder {
        private Long kklOrderId = 0L;
        private String b2bOrderNo = "";
        private Integer dataSourceId = 0;
        private B2BModifyOperationEnum operationType = B2BModifyOperationEnum.NONE;
        private Long changeTime = 0L;
        private Long hopeArrivalTime = 0L;
        private Long operateTime = 0L;
        private Long serviceTime = 0L;
        private String userName = "";
        private String userMobile = "";
        private String userPhone = "";
        private String userAddress = "";
        private String userProvince = "";
        private String userCity = "";
        private String userCounty = "";
        private String userStreet = "";
        private String remarks = "";
        private String expressCompany = "";
        private String expressNo = "";
        private Long deliveryTime = 0L;

        public Builder setKklOrderId(Long kklOrderId) {
            this.kklOrderId = kklOrderId;
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

        public Builder setOperationType(Integer operationType) {
            this.operationType = B2BModifyOperationEnum.get(operationType);
            return this;
        }

        public Builder setChangeTime(Long changeTime) {
            this.changeTime = changeTime;
            return this;
        }

        public Builder setHopeArrivalTime(Long hopeArrivalTime) {
            this.hopeArrivalTime = hopeArrivalTime;
            return this;
        }

        public Builder setOperateTime(Long operateTime) {
            this.operateTime = operateTime;
            return this;
        }

        public Builder setServiceTime(Long serviceTime) {
            this.serviceTime = serviceTime;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserMobile(String userMobile) {
            this.userMobile = userMobile;
            return this;
        }

        public Builder setUserPhone(String userPhone) {
            this.userPhone = userPhone;
            return this;
        }

        public Builder setUserAddress(String userAddress) {
            this.userAddress = userAddress;
            return this;
        }

        public Builder setUserProvince(String userProvince) {
            this.userProvince = userProvince;
            return this;
        }

        public Builder setUserCity(String userCity) {
            this.userCity = userCity;
            return this;
        }

        public Builder setUserCounty(String userCounty) {
            this.userCounty = userCounty;
            return this;
        }

        public Builder setUserStreet(String userStreet) {
            this.userStreet = userStreet;
            return this;
        }

        public Builder setRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder setExpressCompany(String expressCompany) {
            this.expressCompany = expressCompany;
            return this;
        }

        public Builder setExpressNo(String expressNo) {
            this.expressNo = expressNo;
            return this;
        }

        public Builder setDeliveryTime(Long deliveryTime) {
            this.deliveryTime = deliveryTime;
            return this;
        }

        public B2BOrderModifyEntity build() {
            return new B2BOrderModifyEntity(this);
        }
    }

}
