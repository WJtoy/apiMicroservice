package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.common.utils.Exceptions;
import com.wolfking.jeesite.common.utils.RedisUtilsLocal;
import com.kkl.kklplus.utils.StringUtils;
import com.wolfking.jeesite.modules.mq.conf.CreateOrderPushMessageConfig;
import com.wolfking.jeesite.modules.mq.conf.CreateOrderPushMessageRetryConfig;
import com.wolfking.jeesite.modules.mq.dto.MQCreateOrderPushMessage;
import com.wolfking.jeesite.modules.mq.entity.OrderCreateBody;
import com.wolfking.jeesite.modules.mq.service.OrderCreateMessageService;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.modules.sys.utils.SeqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * 下单时发送短信及APP推送
 */
@Component
@Slf4j
public class CreateOrderPushMessageSender implements RabbitTemplate.ConfirmCallback{

    @Autowired
    private OrderCreateMessageService orderCreateMessageService;

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    private RedisUtilsLocal redisUtilsLocal;

    @Autowired
    public CreateOrderPushMessageSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    public void send(MQCreateOrderPushMessage.CreateOrderPushMessage pushMessage) {
//        System.out.println("create order message:" + pushMessage.getOrderId());
        /* 发布时，取消注释 */
        this.rabbitTemplate.convertAndSend
                (
                        CreateOrderPushMessageConfig.MQ_CREATEORDER_PUSH_MESSAGE_EXCHANGE,
                        CreateOrderPushMessageConfig.MQ_CREATEORDER_PUSH_MESSAGE_ROUTING,
                        pushMessage.toByteArray(),
                        new CorrelationData(UUID.randomUUID().toString())
                );

        //redisUtils.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_SS, CreateOrderPushMessageConfig.MQ_CREATEORDER_PUSH_MESSAGE_COUNTER));

    }

    /**
     * 延迟发送消息
     *
     * @param message 消息体
     * @param delay   延迟时间(毫秒)
     * @param times   第几次发送
     */
    public void sendDelay(MQCreateOrderPushMessage.CreateOrderPushMessage message, int delay, int times) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        CreateOrderPushMessageRetryConfig.MQ_CREATEORDER_PUSH_MESSAGE_RETRY,
                        CreateOrderPushMessageRetryConfig.MQ_CREATEORDER_PUSH_MESSAGE_RETRY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(B2BMQConstant.MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQCreateOrderPushMessage.CreateOrderPushMessage msg = MQCreateOrderPushMessage.CreateOrderPushMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                String msgJson = new JsonFormat().printToString(msg);
                OrderCreateBody body = new OrderCreateBody();
                body.setId(msg.getId());
                body.setOrderId(msg.getOrderId());
                body.setQuarter(msg.getQuarter());
                body.setStatus(40);
                body.setTriggerBy(new User(msg.getTriggerBy().getId()));
                body.setTriggerDate(DateUtils.longToDate(msg.getTriggerDate()));
                body.setType(1);
                body.setCreateDate(new Date());
                body.setJson(msgJson);
                body.setRemarks(StringUtils.left(throwable.getMessage(), 243));
                if (body.getId() == null || body.getId() <= 0) {
                    body.setId(SeqUtils.NextID());
                    orderCreateMessageService.insert(body);
                } else {
                    body.setRetryTimes(1);
                    body.setUpdateDate(new Date());
                    orderCreateMessageService.update(body);
                }
                log.error("delay send error {}, {}", Exceptions.getStackTraceAsString(throwable), msg);
                return null;
            });
        } catch (Exception e) {
            log.error("CreateOrderPushMessageSender.sendDelay:{}", Exceptions.getStackTraceAsString(e));
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //if (ack) {
        //    redisUtils.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RS, CreateOrderPushMessageConfig.MQ_CREATEORDER_PUSH_MESSAGE_COUNTER));
        //} else {
        //    redisUtils.incr(RedisConstant.RedisDBType.REDIS_MQ_DB, String.format(RedisConstant.MQ_RE, CreateOrderPushMessageConfig.MQ_CREATEORDER_PUSH_MESSAGE_COUNTER));
        //}
    }
}
