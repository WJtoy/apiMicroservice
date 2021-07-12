package com.wolfking.jeesite.ms.b2bcenter.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BCenterPushOrderInfoToMsMessage;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterPushOrderInfoToMsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class B2BCenterPushOrderInfoToMsMQSender implements RabbitTemplate.ConfirmCallback {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    @Autowired
    public B2BCenterPushOrderInfoToMsMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 正常发送消息
     *
     * @param message 消息体
     */
    public void send(MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        B2BMQConstant.MQ_B2B_CENTER_PUSH_ORDER_INFO_TO_MS,
                        B2BMQConstant.MQ_B2B_CENTER_PUSH_ORDER_INFO_TO_MS,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage msg = MQB2BCenterPushOrderInfoToMsMessage.B2BCenterPushOrderInfoToMsMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                B2BCenterPushOrderInfoToMsService.saveFailureLog("B2BCenterPushOrderInfoToMsMQSender.send", new JsonFormat().printToString(msg), new Exception(throwable.getLocalizedMessage()));
                return null;
            });
        } catch (Exception e) {
            B2BCenterPushOrderInfoToMsService.saveFailureLog("B2BCenterPushOrderInfoToMsMQSender.send", new JsonFormat().printToString(message), new Exception(e.getLocalizedMessage()));
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
