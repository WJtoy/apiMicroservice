package com.wolfking.jeesite.ms.globalmapping.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.md.mq.MQConstant;
import com.kkl.kklplus.entity.md.mq.MQProductCategoryProductMappingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCategoryProductMappingMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public ProductCategoryProductMappingMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    public void send(MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_PRODUCTCATEGORY_PRODUCT_MAPPING_RETRY,
                        MQConstant.MS_MQ_PRODUCTCATEGORY_PRODUCT_MAPPING_RETRY,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(MQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage msg = MQProductCategoryProductMappingMessage.ProductCategoryProductMappingMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("[ProductCategoryProductMappingMQSender.send] send error,msg body:{}", msg, throwable);
                return null;
            });
        } catch (Exception e) {
            if (message == null) {
                log.error("[ProductCategoryProductMappingMQSender.send] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(message);
            log.error("[ProductCategoryProductMappingMQSender.send] send error,msg body:{}", msgJson, e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
