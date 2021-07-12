package com.wolfking.jeesite.modules.sd.entity;

import com.google.gson.annotations.JsonAdapter;
import com.wolfking.jeesite.modules.sd.utils.OrderStatusAdapter;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import com.wolfking.jeesite.modules.sys.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态标记表
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderStatusFlag implements Serializable {

    private Long orderId;
    private String quarter = "";//数据库分片
    private int praiseStatus ;//好评单状态
    private int completeStatus;//工单完工状态
}
