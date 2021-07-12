package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.fi.mq.MQConstant;
import com.kkl.kklplus.entity.fi.mq.MQCreateCustomerChargeMessage;
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
public class CreateCustomerChargeSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate createCustomerChargeRabbitTemplate;
    private RetryTemplate createCustomerChargeRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public CreateCustomerChargeSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.createCustomerChargeRabbitTemplate = manualRabbitTemplate;
        this.createCustomerChargeRabbitTemplate.setConfirmCallback(this);
        this.createCustomerChargeRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQCreateCustomerChargeMessage.CreateCustomerChargeMessage createCustomerChargeMessage) throws Exception {
        this.createCustomerChargeRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", createCustomerChargeMessage);
            this.createCustomerChargeRabbitTemplate.convertAndSend(
                    MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE,
                    MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE,
                    createCustomerChargeMessage.toByteArray(),
                    messageProcessor -> {
                        messageProcessor.getMessageProperties().setDelay(2500);
                        return messageProcessor;
                    },
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQCreateCustomerChargeMessage.CreateCustomerChargeMessage msg = MQCreateCustomerChargeMessage.CreateCustomerChargeMessage.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("订单对帐.发送客户对帐队列", "FI:CreateCustomerChargeSender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CHARGE_COUNTER));
        }
    }
}
