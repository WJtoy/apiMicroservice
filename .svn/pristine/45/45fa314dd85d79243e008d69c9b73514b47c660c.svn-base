package com.wolfking.jeesite.ms.logistics.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.lm.SubscribeExpress;
import com.kkl.kklplus.entity.lm.mq.MQConstant;
import com.kkl.kklplus.entity.lm.mq.MQLMExpress;
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
 * 重试订阅队列
 * @author Ryan
 * @date 2018/12/28 21:34
 */
@Slf4j
@Component
public class SubsExpressMQSender implements RabbitTemplate.ConfirmCallback {

    public static final String MESSAGE_PROPERTIES_HEADER_KEY_TIMES = "messagePropertiesHeaderKeyTimes";

    @Autowired
    private RabbitProperties rabbitProperties;

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public SubsExpressMQSender(RabbitTemplate rabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 重试(延迟队列)
     *
     * @param express 物流实体
     * @param times   第几次发送
     */
    public void sendRetry(SubscribeExpress express, int times){
        MQLMExpress.ExpressMessage expressMessage = MQLMExpress.ExpressMessage.newBuilder()
                .setOrderId(express.getOrderId())
                .setOrderNo(express.getOrderNo())
                .setQuarter(express.getQuarter())
                .setGoodsType(MQLMExpress.GoodsType.forNumber(express.getGoodsType()))
                .setPhone(express.getPhone())
                .addItems(MQLMExpress.ExpressItem.newBuilder()
                        .setCompanyCode(express.getExpressCompanyCode())
                        .setCompanyName(express.getExpressCompany())
                        .setNumber(express.getExpressNo())
                        .setGoodsId(express.getGoodsId())
                        .setGoodsName(express.getGoodsName())
                        .setId(express.getId())
                        .build())
                .build();
        int delay = getDelayMillisecs(times);//延时毫秒数
        sendRetry(expressMessage,delay,times);
    }

    /**
     * 重试(延迟队列)
     *
     * @param message 消息体
     * @param delay   延迟时间(毫秒)
     * @param times   第几次发送
     */
    public void sendRetry(MQLMExpress.ExpressMessage message, int delay, int times) {
        if(message == null){
            return;
        }
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_LM_EXPRESS_RETRY,
                        MQConstant.MS_MQ_LM_EXPRESS_RETRY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQLMExpress.ExpressMessage msg = MQLMExpress.ExpressMessage.parseFrom((byte[]) msgObj);
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
