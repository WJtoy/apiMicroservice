package com.wolfking.jeesite.ms.globalmapping.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.md.mq.MQConstant;
import com.kkl.kklplus.entity.md.mq.MQProductCategoryUserMappingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCategoryUserMappingMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public ProductCategoryUserMappingMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    public void send(MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_PRODUCTCATEGORY_USER_MAPPING,
                        MQConstant.MS_MQ_PRODUCTCATEGORY_USER_MAPPING,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage msg = MQProductCategoryUserMappingMessage.ProductCategoryUserMappingMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("[ProductCategoryUserMappingMQSender.send]send error,msg body:{}", msg, throwable);
                return null;
            });
        } catch (Exception e) {
            if (message == null) {
                log.error("[ProductCategoryUserMappingMQSender.send] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(message);
            log.error("[ProductCategoryUserMappingMQSender.send] send error,msg body:{}", msgJson, e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
