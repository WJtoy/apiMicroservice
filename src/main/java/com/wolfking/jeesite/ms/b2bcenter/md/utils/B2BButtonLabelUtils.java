package com.wolfking.jeesite.ms.b2bcenter.md.utils;

import com.kkl.kklplus.entity.b2bcenter.md.B2BDataSourceEnum;

import java.util.HashMap;
import java.util.Map;

public class B2BButtonLabelUtils {

    public enum ButtonEnum {
        /**
         * B2B待转工单列表 - 手动转单
         */
        MANUAL_TRANSFER(10),
        /**
         * B2B待转工单列表 - 忽略隐藏
         */
        IGNORE_AND_HIDE(20),
        /**
         * B2B待转工单列表 - 预约取消
         */
        APPOINT_AND_CANCEL(30),
        /**
         * B2B待转工单列表 - 直接取消
         */
        DIRECTLY_CANCEL(40);

        public int buttonId = 0;

        ButtonEnum(int buttonId) {
            this.buttonId = buttonId;
        }
    }

    private static final Map<String, String> ALL_BUTTON_LABEL_MAP = new HashMap<>();

    static {
        set(B2BDataSourceEnum.TMALL, ButtonEnum.MANUAL_TRANSFER, "手动转单");
        set(B2BDataSourceEnum.TMALL, ButtonEnum.IGNORE_AND_HIDE, "忽略隐藏");
        set(B2BDataSourceEnum.TMALL, ButtonEnum.APPOINT_AND_CANCEL, "预约取消");
        set(B2BDataSourceEnum.TMALL, ButtonEnum.DIRECTLY_CANCEL, "直接取消");

        set(B2BDataSourceEnum.INSE, ButtonEnum.APPOINT_AND_CANCEL, "拒绝工单");
        set(B2BDataSourceEnum.JD, ButtonEnum.APPOINT_AND_CANCEL, "直接取消");
        set(B2BDataSourceEnum.SUNING, ButtonEnum.APPOINT_AND_CANCEL, "直接取消");

        set(B2BDataSourceEnum.CANBO, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.USATON, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.FEIYU, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.KONKA, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.JOYOUNG, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.UM, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.JDUE, ButtonEnum.APPOINT_AND_CANCEL, "忽略隐藏");
        set(B2BDataSourceEnum.PDD, ButtonEnum.APPOINT_AND_CANCEL, "拒绝工单");
    }

    private static void set(B2BDataSourceEnum dataSource, ButtonEnum buttonEnum, String label) {
        String key = String.format("%d:%d", dataSource.id, buttonEnum.buttonId);
        ALL_BUTTON_LABEL_MAP.put(key, label);
    }

    public static String getButtonLabel(int dataSourceId, int buttonId) {
        String key = String.format("%d:%d", dataSourceId, buttonId);
        String label;
        if (!ALL_BUTTON_LABEL_MAP.containsKey(key)) {
            key = String.format("%d:%d", B2BDataSourceEnum.TMALL.id, buttonId);
        }
        return ALL_BUTTON_LABEL_MAP.get(key);
    }
}


