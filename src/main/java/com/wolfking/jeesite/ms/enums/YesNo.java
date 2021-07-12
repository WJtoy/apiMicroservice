package com.wolfking.jeesite.ms.enums;

import com.google.common.collect.Lists;
import com.wolfking.jeesite.modules.sys.entity.Dict;

import java.util.List;

/**
 * 是否
 */
public enum YesNo {
    YES(1, "是"),
    NO(0, "否");

    public int value;
    public String label;

    YesNo(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static List<Dict> getAllYesNo() {
        List<Dict> list = Lists.newArrayList();
        list.add(new  Dict(YES.value, YES.label));
        list.add(new Dict(NO.value, NO.label));
        return list;
    }
}
