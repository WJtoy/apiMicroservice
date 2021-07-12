package com.wolfking.jeesite.modules.api.entity.sd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 催单
 * @date 2019-11-27 18:08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestReminder implements Serializable {
    // 催单单据id
    private long id;
    // 订单id
    private long orderId;
    //分片
    private String quarter;
    // 是否需要回复
    @Builder.Default
    private int isWaitReply;
    // 项目
    List<RestReminderItem> items;
}
