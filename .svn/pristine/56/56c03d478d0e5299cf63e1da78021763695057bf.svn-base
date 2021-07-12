package com.wolfking.jeesite.modules.api.util;

/**
 * @Auther wj
 * @Date 2021/2/25 11:35
 */
public interface FIEnums {

    /**
     * 流水类型
     */
    enum CurrencyTypeENum {
        NONE(0,"无"),
        IN(10, "收入"),
        OUT(20, "支出");

        private int value;
        private String name;

        CurrencyTypeENum(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static CurrencyTypeENum fromValue(int value) {
            for (CurrencyTypeENum type : CurrencyTypeENum.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 付款方式
     */
    enum DepositPaymentTypeENum {
        CASH(10, "现金"),
        UNIONPAY(20,"银联转账"),
        ALIPAY(30, "支付宝"),
        WEIXIN(40,"微信");

        private int value;
        private String name;

        DepositPaymentTypeENum(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static DepositPaymentTypeENum fromValue(int value) {
            for (DepositPaymentTypeENum type : DepositPaymentTypeENum.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 处理方式
     */
    enum DepositActionTypeENum {
        OFFLINE_RECHARGE(10, "线下充值"),
        ORDER_DEDUCTION(20,"订单完成扣款");

        private int value;
        private String name;

        DepositActionTypeENum(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static DepositActionTypeENum fromValue(int value) {
            for (DepositActionTypeENum type : DepositActionTypeENum.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }
}
