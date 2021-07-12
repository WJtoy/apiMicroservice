package com.wolfking.jeesite.modules.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.common.B2BProcessFlag;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.b2b.mq.B2BMQQueueType;
import com.kkl.kklplus.entity.b2b.pb.MQTmallServiceMonitorMessageMessage;
import com.wolfking.jeesite.common.utils.DateUtils;
import com.wolfking.jeesite.modules.mq.conf.OrderGradeMessageConfig;
import com.wolfking.jeesite.modules.mq.conf.OrderGradeRetryMessageConfig;
import com.wolfking.jeesite.modules.mq.dto.MQOrderGradeMessage;
import com.wolfking.jeesite.modules.mq.entity.OrderCreateBody;
import com.wolfking.jeesite.modules.mq.service.OrderCreateMessageService;
import com.wolfking.jeesite.modules.sys.entity.User;
import com.wolfking.jeesite.ms.tmall.mq.service.MqB2bTmallLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 订单客评消息队列生产者
 */
@Slf4j
@Component
public class OrderGradeMessageSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private OrderCreateMessageService orderCreateMessageService;

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public OrderGradeMessageSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 正常发送消息
     *
     * @param message 消息体
     */
    public void send(MQOrderGradeMessage.OrderGradeMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        OrderGradeMessageConfig.MQ_ORDER_GRADE,
                        OrderGradeMessageConfig.MQ_ORDER_GRADE,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQOrderGradeMessage.OrderGradeMessage msg = MQOrderGradeMessage.OrderGradeMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("normal send error {}, {}", throwable.getLocalizedMessage(), msg);
                String msgJson = new JsonFormat().printToString(msg);
                OrderCreateBody body = new OrderCreateBody();
                //body.setId(orderMessage.getId());
                body.setQuarter(msg.getQuarter());
                body.setOrderId(msg.getOrderId());
                body.setRemarks("订单客评消息处理失败");
                body.setStatus(40);//fail
                body.setType(2);
                User createBy = new User(msg.getCreateBy().getId());
                createBy.setName(msg.getCreateBy().getName());
                body.setTriggerBy(createBy);
                body.setTriggerDate(DateUtils.longToDate(msg.getCreateDate()));
                body.setCreateBy(createBy);
                body.setCreateDate(body.getTriggerDate());
                body.setUpdateDate(new Date());
                body.setJson(msgJson);

                orderCreateMessageService.insert(body);
                return null;
            });
        } catch (Exception e) {
            log.error("normal send error {}", message,e);

            String msgJson = new JsonFormat().printToString(message);
            OrderCreateBody body = new OrderCreateBody();
            //body.setId(orderMessage.getId());
            body.setQuarter(message.getQuarter());
            body.setOrderId(message.getOrderId());
            body.setRemarks("订单客评消息处理失败");
            body.setStatus(40);//fail
            body.setType(2);
            User createBy = new User(message.getCreateBy().getId());
            createBy.setName(message.getCreateBy().getName());
            body.setTriggerBy(createBy);
            body.setTriggerDate(DateUtils.longToDate(message.getCreateDate()));
            body.setCreateBy(createBy);
            body.setCreateDate(body.getTriggerDate());
            body.setUpdateDate(new Date());
            body.setJson(msgJson);
            orderCreateMessageService.insert(body);
        }
    }

    /**
     * 发送重试消息
     *
     * @param message 消息体
     * @param delay 延迟时间，单位：毫秒
     * @param times   第几次发送
     */
    public void sendRetry(MQOrderGradeMessage.OrderGradeMessage message,int delay, int times) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        OrderGradeRetryMessageConfig.MQ_ORDER_GRADE_RETRY,
                        OrderGradeRetryMessageConfig.MQ_ORDER_GRADE_RETRY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(B2BMQConstant.MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQOrderGradeMessage.OrderGradeMessage msg = MQOrderGradeMessage.OrderGradeMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("delay send error {}, {}", throwable.getLocalizedMessage(), msg);

                String msgJson = new JsonFormat().printToString(msg);
                OrderCreateBody body = new OrderCreateBody();
                //body.setId(orderMessage.getId());
                body.setQuarter(message.getQuarter());
                body.setOrderId(message.getOrderId());
                body.setRemarks("订单客评消息处理失败");
                body.setStatus(40);//ok
                body.setType(2);
                User createBy = new User(msg.getCreateBy().getId());
                createBy.setName(msg.getCreateBy().getName());
                body.setTriggerBy(createBy);
                body.setTriggerDate(DateUtils.longToDate(msg.getCreateDate()));
                body.setCreateBy(createBy);
                body.setCreateDate(body.getTriggerDate());
                body.setUpdateDate(new Date());
                body.setJson(msgJson);
                orderCreateMessageService.insert(body);
                return null;
            });
        } catch (Exception e) {
            log.error("OrderGradeMessageSender.sendRetry", e);
            try {
                String msgJson = new JsonFormat().printToString(message);
                OrderCreateBody body = new OrderCreateBody();
                //body.setId(orderMessage.getId());
                body.setQuarter(message.getQuarter());
                body.setOrderId(message.getOrderId());
                body.setRemarks("订单客评消息处理失败");
                body.setStatus(40);//ok
                body.setType(2);
                User createBy = new User(message.getCreateBy().getId());
                createBy.setName(message.getCreateBy().getName());
                body.setTriggerBy(createBy);
                body.setTriggerDate(DateUtils.longToDate(message.getCreateDate()));
                body.setCreateBy(createBy);
                body.setCreateDate(body.getTriggerDate());
                body.setUpdateDate(new Date());
                body.setJson(msgJson);
                orderCreateMessageService.insert(body);
            }catch (Exception e1){
                String msgJson = new JsonFormat().printToString(message);
                log.error("发送订单客评消息异常，且保存异常记录失败:{}",msgJson,e1);
            }
        }

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
