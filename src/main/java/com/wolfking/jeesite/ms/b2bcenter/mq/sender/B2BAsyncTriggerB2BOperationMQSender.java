package com.wolfking.jeesite.ms.b2bcenter.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BAsyncTriggerB2BOperationMessage;
import com.wolfking.jeesite.ms.b2bcenter.mq.config.B2BAsyncTriggerB2BOperationMQConfig;
import com.wolfking.jeesite.ms.b2bcenter.sd.service.B2BCenterAsyncTriggerB2BOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class B2BAsyncTriggerB2BOperationMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public B2BAsyncTriggerB2BOperationMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送延迟消息
     */
    public void sendDelay(MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        B2BMQConstant.MQ_B2BCENTER_ASYNC_TRIGGER_B2B_OPERATION_DELAY,
                        B2BMQConstant.MQ_B2BCENTER_ASYNC_TRIGGER_B2B_OPERATION_DELAY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(B2BAsyncTriggerB2BOperationMQConfig.DELAY_MILLISECOND);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage msg = MQB2BAsyncTriggerB2BOperationMessage.B2BAsyncTriggerB2BOperationMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                B2BCenterAsyncTriggerB2BOperationService.saveFailureLog("B2BAsyncTriggerB2BOperationMQReceiver.sendDelay", new JsonFormat().printToString(msg), new Exception(throwable.getLocalizedMessage()));
                return null;
            });
        } catch (Exception e) {
            B2BCenterAsyncTriggerB2BOperationService.saveFailureLog("B2BAsyncTriggerB2BOperationMQReceiver.sendDelay", new JsonFormat().printToString(message), new Exception(e.getLocalizedMessage()));
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
