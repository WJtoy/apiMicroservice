package com.wolfking.jeesite.modules.api.entity.fi.mywallet;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.api.entity.common.AppBaseEntity;
import com.wolfking.jeesite.modules.api.entity.common.AppDict;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppGetServicePointWithdrawListResponse extends AppBaseEntity {
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
     * 提现金额
     */
    @Getter
    @Setter
    private Double withdrawCharge = 0.0;
    /**
     * 提现明细
     */
    @Getter
    @Setter
    private List<WithdrawItem> list = Lists.newArrayList();

    public static class WithdrawItem {
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
         * 结算方式
         */
        @Getter
        @Setter
        private AppDict paymentType = new AppDict();
        /**
         * 退补金额
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
