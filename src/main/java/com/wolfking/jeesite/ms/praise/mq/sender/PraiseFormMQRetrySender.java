package com.wolfking.jeesite.ms.praise.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.praise.dto.MQPraiseMessage;
import com.kkl.kklplus.entity.praise.mq.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * 好评单审核结果消费重试消息
 * @author Ryan
 * @date 2020/03/31 10:59 AM
 */
@Slf4j
@Component
public class PraiseFormMQRetrySender implements RabbitTemplate.ConfirmCallback {

    public static final String MESSAGE_PROPERTIES_HEADER_KEY_TIMES = "messagePropertiesHeaderKeyTimes";

    @Autowired
    private RabbitProperties rabbitProperties;

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public PraiseFormMQRetrySender(RabbitTemplate rabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 重试(延迟队列)
     *
     * @param message 消息体
     * @param times   第几次发送
     */
    public void sendRetry(MQPraiseMessage.PraiseActionMessage message, int times){
        int delay = getDelayMillisecs(times);//延时毫秒数
        sendRetry(message,delay,times);
    }

    /**
     * 重试(延迟队列)
     *
     * @param message 消息体
     * @param delay   延迟时间(毫秒)
     * @param times   第几次发送
     */
    public void sendRetry(MQPraiseMessage.PraiseActionMessage message, int delay, int times) {
        if(message == null){
            return;
        }
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY, message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY,
                        MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY);
                MQPraiseMessage.PraiseActionMessage msg = MQPraiseMessage.PraiseActionMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                String msgJson = new JsonFormat().printToString(msg);
                log.error("[sendRetry] json: {}", msgJson,throwable);
                return null;
            });
        } catch (Exception e) {
            log.error("[sendRetry] json:{}",new JsonFormat().printToString(message), e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }

    protected int getDelayMillisecs(int times) {
        return (int) (rabbitProperties.getTemplate().getRetry().getInitialInterval() * rabbitProperties.getTemplate().getRetry().getMultiplier() * times);
    }
}
