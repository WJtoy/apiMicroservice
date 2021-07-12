package com.wolfking.jeesite.modules.mq.entity;

import com.wolfking.jeesite.common.persistence.LongIDDataEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ryan Lu
 * @version 1.0.0
 * 支付通知处理失败记录表
 * @date 2019-07-29 16:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MQRecharge extends LongIDDataEntity<MQRecharge> {
    //分片
    private String quarter = "";
    //客户
    private long customerId;
    //充值单id
    private long rechargeId;
    //重试次数，失败后再重试3次，超过3次视为失败
    private int retryTimes;
    //状态，10：待处理，20：失败重试中，30：处理成功，40：处理失败
    private int status;
    //消息体(json格式)
    private String messageContent;
    //触发人
    private long triggerBy;
    //触发时间
    private long triggerAt;
    //创建时间
    private long createAt;
    //修改时间
    private long updateAt;
}
