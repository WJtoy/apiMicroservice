package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.md.mq.MQConstant;
import com.kkl.kklplus.entity.md.mq.MQServicePointEngineerMessage;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServicePointEngineerSender implements RabbitTemplate.ConfirmCallback {
    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    public ServicePointEngineerSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQServicePointEngineerMessage.ServicePointEngineerMessage servicePointEngineerMessage) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", servicePointEngineerMessage);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_SERVICEPOINT_ENGINEER,
                        MQConstant.MS_MQ_SERVICEPOINT_ENGINEER,
                        servicePointEngineerMessage.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(10*1000);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                //重试流程正常结束或者达到重试上限后
                Object msgObj = context.getAttribute("message");
                MQServicePointEngineerMessage.ServicePointEngineerMessage msg = MQServicePointEngineerMessage.ServicePointEngineerMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("delay send error,msg body:{}",msg,throwable);
                String msgJson = new JsonFormat().printToString(msg);
                LogUtils.saveLog("MQServicePointEngineerMessage","retry",msgJson,throwable,null);
                return null;
            });
        } catch (Exception e) {
            if(servicePointEngineerMessage == null) {
                log.error("[sendRetry] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(servicePointEngineerMessage);
            log.error("delay send error,msg body:{}",msgJson,e);
            try {
                LogUtils.saveLog("MQServicePointEngineerMessage","retry",msgJson,e,null);
            }catch (Exception e1){
                log.error("保存日志失败,body:{}",msgJson,e1);
            }
        }
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
