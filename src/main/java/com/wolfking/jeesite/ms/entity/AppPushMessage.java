package com.wolfking.jeesite.ms.entity;

import com.kkl.kklplus.entity.push.AppMessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 推送切换为为服务
 */
@NoArgsConstructor
public class AppPushMessage {

    public enum PassThroughType {
        NOTIFICATION(0, "通知"),
        TRANSPARENT_TRANSMISSION(1, "透传");

        public Integer value;
        public String name;
        private PassThroughType(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    @Getter
    @Setter
    private Long userId = 0L;

    @Getter
    @Setter
    private String title = "";

    @Getter
    @Setter
    private String description = "";

    /**
     * 0-系统消息、1-新单提醒、2-派单提醒、3-配件已审核、4-配件已拒绝、5-配件已发货、6-已付款
     */
    @Getter
    @Setter
    private AppMessageType messageType = AppMessageType.SYSTEM;

    @Getter
    @Setter
    private String subject = "";

    @Getter
    @Setter
    private Long timestamp = 0L;

    @Getter
    @Setter
    private String content = "";

    @Getter
    @Setter
    private PassThroughType passThroughType = PassThroughType.NOTIFICATION;
}
