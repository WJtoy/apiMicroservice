package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.md.mq.MQConstant;
import com.kkl.kklplus.entity.md.mq.MQServicePointPriceMessage;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServicePointPriceSender implements RabbitTemplate.ConfirmCallback {
    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    public ServicePointPriceSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQServicePointPriceMessage.ServicePointPriceMessage servicePointPriceMessage) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", servicePointPriceMessage);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_SERVICEPOINT_PRICE,
                        MQConstant.MS_MQ_SERVICEPOINT_PRICE,
                        servicePointPriceMessage.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(10*1000);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                //重试流程正常结束或者达到重试上限后
                Object msgObj = context.getAttribute("message");
                MQServicePointPriceMessage.ServicePointPriceMessage msg = MQServicePointPriceMessage.ServicePointPriceMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("delay send error,msg body:{}",msg,throwable);
                String msgJson = new JsonFormat().printToString(msg);
                LogUtils.saveLog("MQServicePointEngineerMessage","retry",msgJson,throwable,null);
                return null;
            });
        } catch (Exception e) {
            if(servicePointPriceMessage == null) {
                log.error("[sendRetry] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(servicePointPriceMessage);
            log.error("delay send error,msg body:{}",msgJson,e);
            try {
                LogUtils.saveLog("MQServicePointPriceMessage","retry",msgJson,e,null);
            }catch (Exception e1){
                log.error("保存日志失败,body:{}",msgJson,e1);
            }
        }
    }


    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }
}
