package com.wolfking.jeesite.ms.um.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.um.mq.message.MQB2BUmOrderMessage;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UMPushOrderWebToMSMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public UMPushOrderWebToMSMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 正常发送消息
     *
     * @param message 消息体
     */
    public void send(MQB2BUmOrderMessage.B2BUmOrderMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        B2BMQConstant.MQ_UM_PUSH_ORDER_WEB_TO_MS,
                        B2BMQConstant.MQ_UM_PUSH_ORDER_WEB_TO_MS,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQB2BUmOrderMessage.B2BUmOrderMessage msg = MQB2BUmOrderMessage.B2BUmOrderMessage.parseFrom((byte[]) msgObj);
                String msgJson = new JsonFormat().printToString(msg);
                LogUtils.saveLog("UMPushOrderWebToMSMQSender.send", "发送给优盟微服务的消息", msgJson, null, null);
                return null;
            });
        } catch (Exception e) {
            LogUtils.saveLog("UMPushOrderWebToMSMQSender.send", "", "", e, null);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
