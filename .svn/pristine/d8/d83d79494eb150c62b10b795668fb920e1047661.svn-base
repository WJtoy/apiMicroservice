package com.wolfking.jeesite.modules.sys.entity;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * 是否可见标记枚举
 */
public enum VisibilityFlagEnum {
    NONE(0, "不可见"),
    KEFU(1, "客服可见"),
    CUSTOMER(2, "客户可见"),
    SERVICE_POINT(4, "网点可见");

    private static Map<Integer, VisibilityFlagEnum> map = Maps.newHashMap();

    static {
        for (VisibilityFlagEnum item : VisibilityFlagEnum.values()) {
            map.put(item.getValue(), item);
        }
    }

    private int value;
    private String label;

    VisibilityFlagEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * 对一组可见标记进行或运算
     */
    public static int or(Set<VisibilityFlagEnum> flags) {
        int value = 0;
        if (flags != null && !flags.isEmpty()) {
            for (VisibilityFlagEnum item : flags) {
                value = value | item.getValue();
            }
        }
        return value;
    }

    public static int subtract(Integer value, VisibilityFlagEnum flag) {
        return subtract(value, Sets.newHashSet(flag));
    }

    /**
     * 对可见性数值做减法运算(减去一组可见性标记枚举值)
     */
    public static int subtract(Integer value, Set<VisibilityFlagEnum> flags) {
        Set<VisibilityFlagEnum> resultFlags = Sets.newHashSet();
        if (value != null && value > 0 && flags != null && !flags.isEmpty()) {
            Set<VisibilityFlagEnum> valueFlags = valueOf(value);
            for (VisibilityFlagEnum item : valueFlags) {
                if (!flags.contains(item)) {
                    resultFlags.add(item);
                }
            }
        }
        return or(resultFlags);
    }

    public static int add(Integer value, VisibilityFlagEnum flag) {
        return add(value, Sets.newHashSet(flag));
    }

    /**
     * 对可见性数值做加法运算(加上一组可见性标记枚举值)
     */
    public static int add(Integer value, Set<VisibilityFlagEnum> flags) {
        Set<VisibilityFlagEnum> resultFlags = Sets.newHashSet();
        if (value != null && value > 0 && flags != null && !flags.isEmpty()) {
            Set<VisibilityFlagEnum> valueFlags = valueOf(value);
            resultFlags.addAll(valueFlags);
            resultFlags.addAll(flags);
        }
        return or(resultFlags);
    }

    /**
     * 将数值转换成可见标记枚举
     */
    public static Set<VisibilityFlagEnum> valueOf(Integer value) {
        Set<VisibilityFlagEnum> flags = Sets.newHashSet();
        if (value != null && value > 0) {
            for (VisibilityFlagEnum item : VisibilityFlagEnum.values()) {
                if (item.getValue() == (item.getValue() & value)) {
                    flags.add(item);
                }
            }
        }
        return flags;
    }

    public static boolean has(Integer value, VisibilityFlagEnum flag) {
        return has(value, Sets.newHashSet(flag));
    }

    /**
     * 检查value是否包含flags
     */
    public static boolean has(Integer value, Set<VisibilityFlagEnum> flags) {
        boolean flag = false;
        if (value != null && value > 0 && flags != null && !flags.isEmpty()) {
            Set<VisibilityFlagEnum> valueFlags = valueOf(value);
            if (!valueFlags.isEmpty()) {
                flag = true;
                for (VisibilityFlagEnum item : flags) {
                    if (!valueFlags.contains(item)) {
                        flag = false;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
