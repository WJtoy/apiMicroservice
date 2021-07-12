package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Auther wj
 * @Date 2021/2/22 16:23
 */
public class AppGetServicePointDepositListResponse {

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
     * 完工转金额(按月份)
     */
    @Getter
    @Setter
    private Double orderDeposit = 0.0;

    /***
     * 充值质保金额（按月份）
     */
    @Setter
    @Getter
    private Double rechargeDeposit = 0.0;
    /**
     * 质保金单月
     */
    @Setter
    @Getter
    private Double deposit = 0.0;

    @Getter
    @Setter
    private Double totalDeposit = 0.0;

    @Setter
    @Getter
    private Double totalOrderDeposit = 0.0;

    @Getter
    @Setter
    private Double totalRechargeDeposit = 0.0;

    /**
     * 质保金明细
     */
    @Getter
    @Setter
    private List<AppGetServicePointDepositListResponse.DepositItem> list = Lists.newArrayList();

    public static class DepositItem {
        /**
         * 明细项目ID
         */
        @Getter
        @Setter
        private Long itemId = 0L;
        /**
         * 交易类型
         */
        @Getter
        @Setter
        private AppDict transactionType = new AppDict();

        /**
         * 支付类型
         */
        @Getter
        @Setter
        private AppDict paymentType = new AppDict();

        /**
         * 金额
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
        /**
         * 备注
         */
        @Getter
        @Setter
        private String remarks = "";
    }

}
