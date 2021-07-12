package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderChargeConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderCharge;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.MQ_RE;
import static com.wolfking.jeesite.common.config.redis.RedisConstant.MQ_RS;

@Component
public class OrderChargeSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate orderChargeRabbitTemplate;
    private RetryTemplate orderChargeRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderChargeSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.orderChargeRabbitTemplate = manualRabbitTemplate;
        this.orderChargeRabbitTemplate.setConfirmCallback(this);
        this.orderChargeRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQOrderCharge.OrderCharge orderCharge) throws Exception {
        this.orderChargeRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", orderCharge);
            this.orderChargeRabbitTemplate.convertAndSend(
                    OrderChargeConfig.MQ_ORDER_CHARGE,
                    OrderChargeConfig.MQ_ORDER_CHARGE,
                    orderCharge.toByteArray(),
                    messageProcessor -> {
                        messageProcessor.getMessageProperties().setDelay(45*1000);
                        return messageProcessor;
                    },
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, OrderChargeConfig.MQ_ORDER_CHARGE_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQOrderCharge.OrderCharge msg = MQOrderCharge.OrderCharge.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("订单对帐.自动对帐队列", "FI:OrderChargeSender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, OrderChargeConfig.MQ_ORDER_CHARGE_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, OrderChargeConfig.MQ_ORDER_CHARGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, OrderChargeConfig.MQ_ORDER_CHARGE_COUNTER));
        }
    }
}
