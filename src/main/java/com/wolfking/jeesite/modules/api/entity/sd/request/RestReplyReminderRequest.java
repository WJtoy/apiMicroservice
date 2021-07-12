package com.wolfking.jeesite.modules.api.entity.sd.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 催单回复
 */
@Setter
@Getter
public class RestReplyReminderRequest {
    //id
    private String id;
    //数据库id
    private String orderId;
    //数据库分片
    private String quarter;
    //用户电话,限制输入：20
    private String userPhone;
    //沟通内容，限制输入：150
    private String content;
    //预计完成日期
    private String date;
}
