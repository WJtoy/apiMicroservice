package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderAutoCompleteConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderAutoComplete;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderAutoCompleteDelaySender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate orderAutoCompleteRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderAutoCompleteDelaySender(RabbitTemplate manualRabbitTemplate) {
        this.orderAutoCompleteRabbitTemplate = manualRabbitTemplate;
        this.orderAutoCompleteRabbitTemplate.setConfirmCallback(this);
    }

    public void send(MQOrderAutoComplete.OrderAutoComplete message) {
        this.orderAutoCompleteRabbitTemplate.convertAndSend(
                OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_DELAY,
                OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_DELAY,
                message.toByteArray(),
                msg -> {
                    msg.getMessageProperties().setDelay(OrderAutoCompleteConfig.DELAY_MILLISECOND);
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return msg;
                },
                new CorrelationData(UUID.randomUUID().toString())
        );

        redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
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
