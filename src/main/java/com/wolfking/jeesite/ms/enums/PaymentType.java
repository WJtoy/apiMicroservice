package com.wolfking.jeesite.ms.enums;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.util.List;

/**
 * 结算方式
 */
public enum PaymentType {

    MONTHLY(10, "月结"),
    IMMEDIATELY(20, "即结"),
    BEFOREHAND(30, "预付");

    public int value;
    public String label;

    PaymentType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Dict> getAllPaymentTypes() {
        List<Dict> list = Lists.newArrayList();
        list.add(new  Dict(MONTHLY.value, MONTHLY.label));
        list.add(new Dict(IMMEDIATELY.value, IMMEDIATELY.label));
        list.add(new Dict(BEFOREHAND.value, BEFOREHAND.label));
        return list;
    }

}
