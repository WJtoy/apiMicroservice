package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.ShortMessageConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.*;

/**
 * 短信发送
 */
@Component
public class ShortMessageSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate shortMessageRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public ShortMessageSender(RabbitTemplate manualRabbitTemplate){
        this.shortMessageRabbitTemplate = manualRabbitTemplate;
        this.shortMessageRabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, ShortMessageConfig.MQ_SHORTMESSAGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, ShortMessageConfig.MQ_SHORTMESSAGE_COUNTER));
        }
    }
}
