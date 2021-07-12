package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.fi.mq.MQConstant;
import com.kkl.kklplus.entity.fi.mq.MQCreateCustomerCurrencyMessage;
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
 * 生成客户流水数据发送类
 * @author: Jeff.Zhao
 * @date: 2018/9/3 15:16
 */
@Component
public class CreateCustomerCurrencySender implements RabbitTemplate.ConfirmCallback{
    private RabbitTemplate createCustomerCurrencyRabbitTemplate;
    private RetryTemplate createCustomerCurrencyRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public CreateCustomerCurrencySender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.createCustomerCurrencyRabbitTemplate = manualRabbitTemplate;
        this.createCustomerCurrencyRabbitTemplate.setConfirmCallback(this);
        this.createCustomerCurrencyRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQCreateCustomerCurrencyMessage.CreateCustomerCurrencyMessage createCustomerCurrencyMessage) throws Exception {
        this.createCustomerCurrencyRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", createCustomerCurrencyMessage);
            this.createCustomerCurrencyRabbitTemplate.convertAndSend(
                    MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY,
                    MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY,
                    createCustomerCurrencyMessage.toByteArray(),
                    messageProcessor -> {
                        messageProcessor.getMessageProperties().setDelay(10*1000);
                        return messageProcessor;
                    },
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQCreateCustomerCurrencyMessage.CreateCustomerCurrencyMessage msg = MQCreateCustomerCurrencyMessage.CreateCustomerCurrencyMessage.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("财务.发送客户流水队列", "FI:CreateCustomerCurrencySender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_FI_CREATE_CUSTOMER_CURRENCY_COUNTER));
        }
    }
}
