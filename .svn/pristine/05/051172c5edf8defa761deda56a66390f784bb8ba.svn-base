package com.wolfking.jeesite.modules.mq.sender;

import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.mq.conf.OrderReportConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderReport;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.wolfking.jeesite.common.config.redis.RedisConstant.*;

/**
 * 订单报表统计消息
 */
@Component
public class OrderReportSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate orderReportRabbitTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public OrderReportSender(RabbitTemplate manualRabbitTemplate){
        this.orderReportRabbitTemplate = manualRabbitTemplate;
        this.orderReportRabbitTemplate.setConfirmCallback(this);
    }

    public void send(MQOrderReport.OrderReport message) {
        this.orderReportRabbitTemplate.convertAndSend
                (
                        OrderReportConfig.MQ_ORDER_REPORT_EXCHANGE,
                        OrderReportConfig.MQ_ORDER_REPORT_ROUTING,
                        message.toByteArray(),
                        new CorrelationData(UUID.randomUUID().toString())
                );

        redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_SS, OrderReportConfig.MQ_ORDER_REPORT_COUNTER));
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RS, OrderReportConfig.MQ_ORDER_REPORT_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(MQ_RE, OrderReportConfig.MQ_ORDER_REPORT_COUNTER));
        }
    }
}
