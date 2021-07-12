package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderAutoCompleteConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderAutoComplete;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.*;

/**
 * APP确认完成，自动客评及对账队列
 * Created by Ryan on 2017/12/05.
 */
@Component
public class OrderAutoChargeSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate orderAutoCompleteRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderAutoChargeSender(RabbitTemplate manualRabbitTemplate){
        this.orderAutoCompleteRabbitTemplate = manualRabbitTemplate;
        this.orderAutoCompleteRabbitTemplate.setConfirmCallback(this);
    }

    public void send(MQOrderAutoComplete.OrderAutoComplete orderAutoComplete) {
        this.orderAutoCompleteRabbitTemplate.convertAndSend(OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_EXCHANGE,
                                                        OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_ROUTING,
                                                        orderAutoComplete.toByteArray(),
                                                        new CorrelationData(UUID.randomUUID().toString()));

        redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_SS, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, OrderAutoCompleteConfig.MQ_ORDER_AUTOCOMPLETE_COUNTER));
        }
    }
}
