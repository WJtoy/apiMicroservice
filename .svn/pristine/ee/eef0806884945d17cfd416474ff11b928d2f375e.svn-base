package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetServicePointCompletedChargeDetailResponse extends AppBaseEntity {
    /**
     * 交易类型
     */
    @Getter
    @Setter
    private AppDict transactionType = new AppDict();
    /**
     * 完工金额(包含平台费、扣点)
     */
    @Getter
    @Setter
    private Double amount = 0.0;
    /**
     * 费用明细
     */
    @Getter
    @Setter
    private ChargeDetail chargeDetail = new ChargeDetail();

    /**
     * 工单信息
     */
    @Getter
    @Setter
    private OrderInfo orderInfo = new OrderInfo();

    public static class ChargeDetail {
        @Getter
        @Setter
        private Double serviceCharge = 0.0;
        @Getter
        @Setter
        private Double expressCharge = 0.0;
        @Getter
        @Setter
        private Double travelCharge = 0.0;
        @Getter
        @Setter
        private Double materialCharge = 0.0;
        @Getter
        @Setter
        private Double otherCharge = 0.0;

        @Getter
        @Setter
        private Double customerTimeLinessCharge = 0.0;
        @Getter
        @Setter
        private Double timeLinessCharge = 0.0;
        @Getter
        @Setter
        private Double urgentCharge = 0.0;
        @Getter
        @Setter
        private Double praiseFee = 0.0;

        @Getter
        @Setter
        private Double insuranceCharge = 0.0;

        @Getter
        @Setter
        private String remarks = "";
    }

    public static class OrderInfo {
        @Getter
        @Setter
        private String orderNo = "";
        @Getter
        @Setter
        private String userName = "";
        @Getter
        @Setter
        private String servicePhone = "";
        @Getter
        @Setter
        private String serviceAddress = "";
        @Getter
        @Setter
        private Long chargeDate = 0L;

        @Getter
        @Setter
        private List<ServiceItem> services = Lists.newArrayList();

        public static class ServiceItem {
            @Getter
            @Setter
            private Integer serviceTimes = 0;
            @Getter
            @Setter
            private String productName = "";
            @Getter
            @Setter
            private String serviceTypeName = "";
            @Getter
            @Setter
            private Integer qty = 0;
        }
    }
}
