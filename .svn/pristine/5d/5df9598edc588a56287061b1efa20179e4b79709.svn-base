package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.fi.mq.MQConstant;
import com.kkl.kklplus.entity.fi.mq.MQCreateServicePointChargeMessage;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author: Jeff.Zhao
 * @date: 2018/6/19 16:27
 * @description: 
 **/
@Component
public class CreateServicePointChargeSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate createServicePointChargeRabbitTemplate;
    private RetryTemplate createServicePointChargeRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public CreateServicePointChargeSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.createServicePointChargeRabbitTemplate = manualRabbitTemplate;
        this.createServicePointChargeRabbitTemplate.setConfirmCallback(this);
        this.createServicePointChargeRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQCreateServicePointChargeMessage.CreateServicePointChargeMessage createServicePointChargeMessage) throws Exception {
        this.createServicePointChargeRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", createServicePointChargeMessage);
            this.createServicePointChargeRabbitTemplate.convertAndSend(
                    MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE,
                    MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE,
                    createServicePointChargeMessage.toByteArray(),
                    messageProcessor -> {
                        messageProcessor.getMessageProperties().setDelay(3500);
                        return messageProcessor;
                    },
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQCreateServicePointChargeMessage.CreateServicePointChargeMessage msg = MQCreateServicePointChargeMessage.CreateServicePointChargeMessage.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("订单对帐.发送网点对帐队列", "FI:CreateServicePointChargeSender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_FI_CREATE_SERVICE_POINT_CHARGE_COUNTER));
        }
    }
}
