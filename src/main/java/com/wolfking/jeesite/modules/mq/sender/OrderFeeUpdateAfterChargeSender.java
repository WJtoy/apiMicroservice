package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderFeeUpdateAfterChargeConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderCharge;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.MQ_RE;
import static com.wolfking.jeesite.common.config.redis.RedisConstant.MQ_RS;

/**
 * 对账后更新财务扣费同步到订单
 * @author Ryan
 * @date 2020-04-03
 */
@Slf4j
@Component
public class OrderFeeUpdateAfterChargeSender implements RabbitTemplate.ConfirmCallback{

    private RabbitTemplate orderFeeUpdateAfterChargeRabbitTemplate;
    private RetryTemplate orderFeeUpdateAfterChargeRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderFeeUpdateAfterChargeSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.orderFeeUpdateAfterChargeRabbitTemplate = manualRabbitTemplate;
        this.orderFeeUpdateAfterChargeRabbitTemplate.setConfirmCallback(this);
        this.orderFeeUpdateAfterChargeRetryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 发送延迟消息
     * @param orderFeeUpdateAfterCharge 消息体
     * @param delay 延迟时间，单位i：毫秒
     * @param times retry次数
     */
    public void sendDelay(MQOrderCharge.OrderFeeUpdateAfterCharge orderFeeUpdateAfterCharge, int delay, int times) {
        try {
            this.orderFeeUpdateAfterChargeRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", orderFeeUpdateAfterCharge);
                this.orderFeeUpdateAfterChargeRabbitTemplate.convertAndSend(
                        OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_UPDATE_AFTER_CHARGE,
                        OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_UPDATE_AFTER_CHARGE,
                        orderFeeUpdateAfterCharge.toByteArray(),
                        messageProcessor -> {
                            messageProcessor.getMessageProperties().setDelay(delay);
                            messageProcessor.getMessageProperties().setHeader(B2BMQConstant.MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            messageProcessor.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return messageProcessor;
                        },
                        new CorrelationData(UUID.randomUUID().toString()));
                redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_AFTER_CHARGE_COUNTER));
                return null;
            }, context -> {
                Object objMsg = context.getAttribute("message");
                MQOrderCharge.OrderFeeUpdateAfterCharge msg = MQOrderCharge.OrderFeeUpdateAfterCharge.parseFrom((byte[]) objMsg);
                Throwable t = context.getLastThrowable();
                try {
                    LogUtils.saveLog("订单对帐后.更新财务扣费队列", "OrderFeeUpdateAfterChargeSender", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
                } catch (Exception e) {
                }
                redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_AFTER_CHARGE_COUNTER));
                return null;
            });
        } catch (Exception e) {
            log.error("OrderFeeUpdateAfterChargeSender.sendDelay:{}",new JsonFormat().printToString(orderFeeUpdateAfterCharge),e);
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_AFTER_CHARGE_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, OrderFeeUpdateAfterChargeConfig.MQ_ORDER_FEE_AFTER_CHARGE_COUNTER));
        }
    }
}
