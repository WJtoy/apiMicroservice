package com.wolfking.jeesite.modules.api.entity.sd;

import com.kkl.kklplus.entity.cc.AbnormalForm;
import com.wolfking.jeesite.modules.sd.entity.OrderOpitionTrace;
import com.wolfking.jeesite.modules.sd.entity.Order;
import com.wolfking.jeesite.modules.sys.entity.Dict;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * App反馈
 * @author Ryan Lu
 * @version 1.0.0
 * @date 2020-01-08
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestAppFeedback implements Serializable {

    // 订单id
    private long orderId;
    //分片
    private String quarter;

    private Order order;

    private Dict pendingType;

    private long appointmentAt;

    private OrderOpitionTrace orderOpitionTrace;

    private AbnormalForm abnormalForm;
}
