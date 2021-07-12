package com.wolfking.jeesite.modules.mq.sender;

import com.kkl.kklplus.entity.push.MQAppPushMessage;
import com.kkl.kklplus.entity.push.PushConstant;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * MQ切换为微服务
 */
@Component
public class PushMessageSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate pushRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public PushMessageSender(RabbitTemplate manualRabbitTemplate){
        this.pushRabbitTemplate = manualRabbitTemplate;
        this.pushRabbitTemplate.setConfirmCallback(this);
    }

    /**
     *
     * @param pushMessage
     */
    public void send(MQAppPushMessage.PushMessage pushMessage) {
        this.pushRabbitTemplate.convertAndSend(PushConstant.MQ_PUSH_MESSAGE_EXCHANGE,
                PushConstant.MQ_PUSH_MESSAGE_ROUTING,
                pushMessage.toByteArray(),
                new CorrelationData(UUID.randomUUID().toString()));

        redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, PushConstant.MQ_PUSH_MESSAGE_COUNTER));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, PushConstant.MQ_PUSH_MESSAGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, PushConstant.MQ_PUSH_MESSAGE_COUNTER));
        }
    }
}
