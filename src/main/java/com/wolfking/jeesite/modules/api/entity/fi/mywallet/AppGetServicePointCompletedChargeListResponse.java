package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetServicePointCompletedChargeListResponse extends AppBaseEntity {
    /**
     * 页码
     */
    @Getter
    @Setter
    private Integer pageNo = 1;
    /**
     * 页尺寸
     */
    @Getter
    @Setter
    private Integer pageSize = 10;
    /**
     * 行数
     */
    @Getter
    @Setter
    private Integer rowCount = 0;
    /**
     * 页数
     */
    @Getter
    @Setter
    private Integer pageCount = 0;

    /**
     * 年份，如2020
     */
    @Getter
    @Setter
    private Integer yearIndex = 0;
    /**
     * 月份，如6
     */
    @Getter
    @Setter
    private Integer monthIndex = 0;
    /**
     * 完工单金额
     */
    @Getter
    @Setter
    private Double completedCharge = 0.0;
    /**
     * 完工单总数量
     */
    @Getter
    @Setter
    private Integer completedQty = 0;
    /**
     * 退补单总数量
     */
    @Getter
    @Setter
    private Integer writeOffQty = 0;
    /**
     * 扣点
     */
    @Getter
    @Setter
    private Double taxFee = 0.0;
    /**
     * 平台费
     */
    @Setter
    @Getter
    private Double infoFee = 0.0;
    /**
     * 完工单金额明细
     */
    @Getter
    @Setter
    private List<CompletedChargeItem> list = Lists.newArrayList();

    public static class CompletedChargeItem {
        /**
         * 明细项目ID
         */
        @Getter
        @Setter
        private Long itemId = 0L;
        /**
         * 工单ID
         */
        @Getter
        @Setter
        private Long orderId = 0L;
        /**
         * 项目所在的分片
         */
        @Getter
        @Setter
        private String quarter = "";
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
         * 关联单号
         */
        @Getter
        @Setter
        private String currencyNo = "";
        /**
         * 创建时间
         */
        @Getter
        @Setter
        private Long createDate = 0L;
    }
}
