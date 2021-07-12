package com.wolfking.jeesite.modules.mq.sender.voice;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.voiceservice.mq.MQVoiceSeviceMessage;
import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * 新营销任务队列
 * @author Ryan
 * @date 2018/12/28 21:34
 */
@Slf4j
@Component
public class NewTaskMQSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public NewTaskMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送普通队列
     *
     * @param message 消息体
     */
    public void send(MQVoiceSeviceMessage.Task message) {
        if(message == null){
            return;
        }
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", message);
                rabbitTemplate.convertAndSend(
                        VoiceServiceMQConstant.MQ_VOICE_SEND_NEW_TASK,
                        VoiceServiceMQConstant.MQ_VOICE_SEND_NEW_TASK,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute("message");
                MQVoiceSeviceMessage.Task msg = MQVoiceSeviceMessage.Task.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                String msgJson = new JsonFormat().printToString(msg);
                log.error("[发送新营销任务失败] json: {}", msgJson,throwable);
                return null;
            });
        } catch (Exception e) {
            log.error("[发送新营销任务失败] json:{}",new JsonFormat().printToString(message), e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
