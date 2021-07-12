package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderAutoCompleteConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 下单时发送短信及APP推送
 */
@Component
public class OrderAutoCompleteSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate orderAutoCompleteRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderAutoCompleteSender(RabbitTemplate manualRabbitTemplate){
        this.orderAutoCompleteRabbitTemplate = manualRabbitTemplate;
        this.orderAutoCompleteRabbitTemplate.setConfirmCallback(this);
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
        }
    }
}
