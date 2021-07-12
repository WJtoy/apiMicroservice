package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.ServicePointOrderRetryMessageConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderServicePointMessage;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * 订单网点消息队列生产者
 */
@Slf4j
@Component
public class OrderServicePointMessageSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public OrderServicePointMessageSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送重试消息
     *
     * @param message 消息体
     * @param delay 延迟时间，单位：毫秒
     * @param times   第几次发送
     */
    public void sendRetry(MQOrderServicePointMessage.ServicePointMessage message, int delay, int times) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        ServicePointOrderRetryMessageConfig.MQ_SERVICEPOINT_ORDER_RETRY,
                        ServicePointOrderRetryMessageConfig.MQ_SERVICEPOINT_ORDER_RETRY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(B2BMQConstant.MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                //重试流程正常结束或者达到重试上限后
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQOrderServicePointMessage.ServicePointMessage msg = MQOrderServicePointMessage.ServicePointMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("delay send error,msg body:{}",msg,throwable);
                String msgJson = new JsonFormat().printToString(msg);
                LogUtils.saveLog("MQOrderServicePoint","retry",msgJson,throwable,null);
                return null;
            });
        } catch (Exception e) {
            if(message == null) {
                log.error("[sendRetry] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(message);
            log.error("delay send error,msg body:{}",msgJson,e);
            try {
                LogUtils.saveLog("MQOrderServicePointMessage","retry",msgJson,e,null);
            }catch (Exception e1){
                log.error("保存日志失败,body:{}",msgJson,e1);
            }
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
