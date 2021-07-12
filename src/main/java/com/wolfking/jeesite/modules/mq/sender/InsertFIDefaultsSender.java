package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.fi.mq.MQConstant;
import com.kkl.kklplus.entity.fi.mq.MQInserDefaultsMessage;
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
 * @Author: Jeff.Zhao
 * @Date: 2018/8/8 10:35
 * @Description:
 **/
@Component
public class InsertFIDefaultsSender implements RabbitTemplate.ConfirmCallback{
    private RabbitTemplate insertFIDefaultsRabbitTemplate;
    private RetryTemplate insertFIDefaultsRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public InsertFIDefaultsSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.insertFIDefaultsRabbitTemplate = manualRabbitTemplate;
        this.insertFIDefaultsRabbitTemplate.setConfirmCallback(this);
        this.insertFIDefaultsRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQInserDefaultsMessage.InsertDefaultsMessage insertDefaultsMessage) throws Exception {
        this.insertFIDefaultsRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", insertDefaultsMessage);
            this.insertFIDefaultsRabbitTemplate.convertAndSend(
                    MQConstant.MS_MQ_FI_INSERT_DEFAULTS,
                    MQConstant.MS_MQ_FI_INSERT_DEFAULTS,
                    insertDefaultsMessage.toByteArray(),
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_FI_INSERT_DEFAULTS_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQInserDefaultsMessage.InsertDefaultsMessage msg = MQInserDefaultsMessage.InsertDefaultsMessage.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("订单对帐.发送网点对帐队列", "FI:CreateServicePointChargeSender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, MQConstant.MS_MQ_FI_INSERT_DEFAULTS_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_FI_INSERT_DEFAULTS_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_FI_INSERT_DEFAULTS_COUNTER));
        }
    }
}
