package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.fi.mq.MQConstant;
import com.kkl.kklplus.entity.fi.mq.MQCreateUpdateServicePointDeductedMessage;
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
 * 生成网点流水数据发送类
 * @author: Jeff.Zhao
 * @date: 2018/10/12 14:48
 */
@Component
public class CreateUpdateServicePointDeductedSender implements RabbitTemplate.ConfirmCallback{
    private RabbitTemplate createUpdateServicePointDeductedRabbitTemplate;
    private RetryTemplate createUpdateServicePointDeductedRetryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public CreateUpdateServicePointDeductedSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate){
        this.createUpdateServicePointDeductedRabbitTemplate = manualRabbitTemplate;
        this.createUpdateServicePointDeductedRabbitTemplate.setConfirmCallback(this);
        this.createUpdateServicePointDeductedRetryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQCreateUpdateServicePointDeductedMessage.CreateUpdateServicePointDeductedMessage createUpdateServicePointDeductedMessage) throws Exception {
        this.createUpdateServicePointDeductedRetryTemplate.execute((RetryCallback<Object, Exception>) context -> {
            context.setAttribute("message", createUpdateServicePointDeductedMessage);
            this.createUpdateServicePointDeductedRabbitTemplate.convertAndSend(
                    MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED,
                    MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED,
                    createUpdateServicePointDeductedMessage.toByteArray(),
                    new CorrelationData(UUID.randomUUID().toString()));
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED_COUNTER));
            return null;
        }, context -> {
            Object objMsg = context.getAttribute("message");
            MQCreateUpdateServicePointDeductedMessage.CreateUpdateServicePointDeductedMessage msg =
                    MQCreateUpdateServicePointDeductedMessage.CreateUpdateServicePointDeductedMessage.parseFrom((byte[]) objMsg);
            Throwable t = context.getLastThrowable();
            try {
                LogUtils.saveLog("财务.发送网点抵扣队列", "FI:CreateUpdateServicePointDeductedSender.send", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
            } catch (Exception e){}
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SE, MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED_COUNTER));
            return null;
        });
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED_COUNTER));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_FI_CREATE_UPDATE_SERVICE_POINT_DEDUCTED_COUNTER));
        }
    }
}
