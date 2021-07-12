package com.wolfking.jeesite.modules.mq.sender;

import com.kkl.kklplus.entity.rpt.mq.MQRPTCreateOrderMessage;
import com.kkl.kklplus.entity.rpt.mq.RPTMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class RPTCreatedOrderSender implements RabbitTemplate.ConfirmCallback {


    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public RPTCreatedOrderSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }


    /**
     * 正常发送消息
     *
     * @param message 消息体
     */
    public boolean send(MQRPTCreateOrderMessage.RPTCreateOrderMessage message) {
        AtomicBoolean result = new AtomicBoolean(false);
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(RPTMQConstant.MQ_RPT_CREATE_ORDER, message);
                rabbitTemplate.convertAndSend(
                        RPTMQConstant.MQ_RPT_CREATE_ORDER,
                        RPTMQConstant.MQ_RPT_CREATE_ORDER,
                        message.toByteArray(),
                        new CorrelationData());
                result.set(true);
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(RPTMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQRPTCreateOrderMessage.RPTCreateOrderMessage msg = MQRPTCreateOrderMessage.RPTCreateOrderMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("normal send error {}, {}", throwable.getLocalizedMessage(), msg);
                return null;
            });
        } catch (Exception e) {
            log.error("RPTCreatedOrderSender.send：{}", e.getLocalizedMessage());
        }
        return result.get();
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
