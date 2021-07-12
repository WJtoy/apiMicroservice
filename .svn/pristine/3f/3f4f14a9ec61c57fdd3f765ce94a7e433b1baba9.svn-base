package com.wolfking.jeesite.modules.mq.sender.sms;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.voiceservice.mq.MQSmsCallbackMessage;
import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.common.config.WebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;


/**
 * 短信回访队列
 * @author Ryan
 * @date 2019/02/26
 */
@Configurable
@Slf4j
@Component
public class SmsCallbackTaskMQSender implements RabbitTemplate.ConfirmCallback {

//    @Value("${site.code}")
    private String siteCode;

    @Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.siteCode = webProperties.getSite().getCode();
    }

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public SmsCallbackTaskMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 普通发送
     */
    public void send(Long orderId, String quarter, String mobile, String content, String templateCode, String params, String extNo, long sender, Long sendedAt){
        if(orderId == null || orderId.longValue()<=0 ||StringUtils.isBlank(quarter)){
            return;
        }
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(content)){
            return;
        }
        MQSmsCallbackMessage.SmsTask.Builder builder = MQSmsCallbackMessage.SmsTask.newBuilder()
                .setSite(siteCode == null?"":siteCode)
                .setOrderId(orderId)
                .setQuarter(quarter)
                .setMobile(mobile)
                .setContent(content)
                .setTemplateCode(templateCode==null? "" : templateCode)
                .setParams(params==null? "" : params)
                .setExtNo(extNo==null?"":extNo)
                .setTriggerBy(sender)
                .setTriggerDate(sendedAt==null?System.currentTimeMillis():sendedAt)
                .setSendTime(System.currentTimeMillis());
        send(builder.build());
    }

    /**
     * 普通队列
     *
     * @param message 消息体
     */
    public void send(MQSmsCallbackMessage.SmsTask message) {
        if(message == null){
            return;
        }
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", message);
                rabbitTemplate.convertAndSend(
                        VoiceServiceMQConstant.MQ_SMS_CALLBACK_TASK,
                        VoiceServiceMQConstant.MQ_SMS_CALLBACK_TASK,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute("message");
                MQSmsCallbackMessage.SmsTask msg = MQSmsCallbackMessage.SmsTask.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                String msgJson = new JsonFormat().printToString(msg);
                log.error("[发送短信回访任务失败] json: {}", msgJson,throwable);
                return null;
            });
        } catch (Exception e) {
            log.error("[发送短信回访任务失败] json:{}",new JsonFormat().printToString(message), e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
