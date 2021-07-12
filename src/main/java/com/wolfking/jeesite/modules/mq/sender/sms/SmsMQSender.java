package com.wolfking.jeesite.modules.mq.sender.sms;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.sys.SysSMSTypeEnum;
import com.kkl.kklplus.entity.sys.mq.MQConstant;
import com.kkl.kklplus.entity.sys.mq.MQSysShortMessage;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.mq.conf.ShortMessageConfig;
import com.wolfking.jeesite.modules.mq.dto.MQShortMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.*;

/**
 * 短信发送
 */
@Component
@Slf4j
public class SmsMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public SmsMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 普通发送
     */
    public void send(String mobile, String content, String extNo, long sender, Long sendedAt) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
            return;
        }
        try {
            MQSysShortMessage.SysShortMessage.Builder builder = MQSysShortMessage.SysShortMessage.newBuilder()
                    .setType("pt")
                    .setMobile(mobile)
                    .setContent(content)
                    .setExtNo(extNo)
                    .setTriggerBy(sender)
                    .setTriggerDate(sendedAt)
                    .setSendTime(System.currentTimeMillis());
            send(builder.build());
        } catch (Exception e) {
            log.error("发送短信错误- mobile:{},content:{}", mobile, content, e);
        }
    }

    /**
     * 普通发送，增加短信类型字段
     * Added by zhoucy 2019-4-9
     */
    public void sendNew(String mobile, String content, String extNo, long sender, Long sendedAt, SysSMSTypeEnum type) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(content)) {
            return;
        }
        try {
            MQSysShortMessage.SysShortMessage.Builder builder = MQSysShortMessage.SysShortMessage.newBuilder()
                    .setType("pt")
                    .setMobile(mobile)
                    .setContent(content)
                    .setExtNo(extNo)
                    .setTriggerBy(sender)
                    .setTriggerDate(sendedAt)
                    .setSendTime(System.currentTimeMillis())
                    .setSmsType(type.getValue());
            send(builder.build());
        } catch (Exception e) {
            log.error("发送短信错误- mobile:{},content:{}", mobile, content, e);
        }
    }

    public void send(MQSysShortMessage.SysShortMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_SYS_SMS,
                        MQConstant.MS_MQ_SYS_SMS,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute("message");
                MQShortMessage.ShortMessage msg = MQShortMessage.ShortMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                String msgJson = new JsonFormat().printToString(msg);
                log.error("[发送短信回访任务失败] json: {}", msgJson, throwable);
                return null;
            });
        } catch (Exception e) {
            log.error("[发送短信回访任务失败] json:{}", new JsonFormat().printToString(message), e);
        }
        redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_SS, ShortMessageConfig.MQ_SHORTMESSAGE_COUNTER));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, ShortMessageConfig.MQ_SHORTMESSAGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, ShortMessageConfig.MQ_SHORTMESSAGE_COUNTER));
        }
    }
}
