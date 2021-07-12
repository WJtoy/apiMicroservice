package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.NoticeMessageConfig;
import com.wolfking.jeesite.modules.mq.dto.MQNoticeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.*;

/**
 * WebSocket发送
 */
@Component
public class NoticeMessageSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate noticeMessageRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public NoticeMessageSender(RabbitTemplate manualRabbitTemplate){
        this.noticeMessageRabbitTemplate = manualRabbitTemplate;
        this.noticeMessageRabbitTemplate.setConfirmCallback(this);
    }

    public void send(MQNoticeMessage.NoticeMessage message) {
        this.noticeMessageRabbitTemplate.convertAndSend
                (
                        NoticeMessageConfig.MQ_NOTICE_EXCHANGE,
                        NoticeMessageConfig.MQ_NOTICE_ROUTING,
                        message.toByteArray(),
                        new CorrelationData(UUID.randomUUID().toString())
                );

        redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_SS, NoticeMessageConfig.MQ_NOTICE_COUNTER));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, NoticeMessageConfig.MQ_NOTICE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, NoticeMessageConfig.MQ_NOTICE_COUNTER));
        }
    }
}
