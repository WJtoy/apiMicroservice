package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.es.mq.MQConstant;
import com.kkl.kklplus.entity.es.mq.MQSyncServicePointMessage;
import com.wolfking.jeesite.common.config.redis.RedisConstant;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ServicePointSender implements RabbitTemplate.ConfirmCallback {
    private RabbitTemplate servicePointTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public ServicePointSender(RabbitTemplate manualRabbitTemplate,RetryTemplate kklRabbitRetryTemplate) {
        this.servicePointTemplate = manualRabbitTemplate;
        this.servicePointTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQSyncServicePointMessage.SyncServicePointMessage servicePointSyncMessage) {
        this.servicePointTemplate.convertAndSend(MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT,
                MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT,
                servicePointSyncMessage.toByteArray(),
                new CorrelationData(UUID.randomUUID().toString()));
        
        redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT));
    }

    public void sendRetry(MQSyncServicePointMessage.SyncServicePointMessage servicePointSyncMessage) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute("message", servicePointSyncMessage);
                servicePointTemplate.convertAndSend(
                        MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT,
                        MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT,
                        servicePointSyncMessage.toByteArray(),
                        messageProcessor -> {
                            messageProcessor.getMessageProperties().setDelay(45*1000);
                            return messageProcessor;
                        },
                        new CorrelationData(UUID.randomUUID().toString()));
                return null;
            }, context -> {
                Object objMsg = context.getAttribute("message");
                MQSyncServicePointMessage.SyncServicePointMessage msg = MQSyncServicePointMessage.SyncServicePointMessage.parseFrom((byte[]) objMsg);
                Throwable t = context.getLastThrowable();
                try {
                    LogUtils.saveLog("es.网点同步队列", "MS:MQ:ES:SYNC:SERVICEPOINT", new JsonFormat().printToString(msg), new Exception(t.getLocalizedMessage()), new User(1L));
                } catch (Exception e){}

                redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT));
                return null;
            });
        } catch (Exception e) {
            if (servicePointSyncMessage == null) {
                log.error("[sendRetry] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(servicePointSyncMessage);
            log.error("delay send error,msg body:{}",msgJson,e);
            try {
                LogUtils.saveLog("ESServicePointSyncMessage","retry",msgJson,e,null);
            }catch (Exception e1){
                log.error("保存日志失败,body:{}",msgJson,e1);
            }
        }
    }



    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        if (ack) {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT));
        } else {
            redisUtilsLocal.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, MQConstant.MS_MQ_ES_SYNC_SERVICEPOINT));
        }
    }
}
