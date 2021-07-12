package com.wolfking.jeesite.ms.praise.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.praise.mq.MQConstant;
import com.kkl.kklplus.entity.praise.dto.MQPraiseMessage;
import com.kkl.kklplus.utils.StringUtils;
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

/**
 * 好评单自动审核消息队列生产者
 */
@Slf4j
@Component
public class PraiseAutoReviewMessageSender implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public PraiseAutoReviewMessageSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     *
     * @param orderId  订单
     * @param quarter   分片
     * @param servicePointId 当前网点
     * @param user  帐号
     * @param userType  用户类型
     * @param triggerAt 触发日期
     * @param visibilityFlag    日志类型
     * @param delay     队列延迟时间(毫秒)
     * @param times     retry次数
     */
    public void sendRetry(long orderId, String quarter,long servicePointId, User user,int userType,long triggerAt, int visibilityFlag,int delay, int times) {
        if(orderId <=0 || StringUtils.isBlank(quarter)){
            return;
        }
        try {
            MQPraiseMessage.PraiseActionMessage message = MQPraiseMessage.PraiseActionMessage.newBuilder()
                    .setOrderId(orderId)
                    .setQuarter(quarter)
                    .setServicePointId(servicePointId)
                    .setTrigger(MQPraiseMessage.User.newBuilder()
                            .setId(user.getId())
                            .setName(user.getName())
                            .setUserType(userType)
                            .build())
                    .setTriggerAt(triggerAt)
                    .setVisibilityFlag(visibilityFlag)
                    .build();
            sendRetry(message, delay, times);

        }catch (Exception e){
            log.error("发送好评单自动审核消息错误,orderId:{}",orderId,e);
        }
    }

    /**
     * 发送重试消息
     *
     * @param message 消息体
     * @param delay 延迟时间，单位：毫秒
     * @param times   第几次发送
     */
    private void sendRetry(MQPraiseMessage.PraiseActionMessage message, int delay, int times) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        MQConstant.MS_MQ_PRAISE_REVIEW_DELAY,
                        MQConstant.MS_MQ_PRAISE_REVIEW_DELAY,
                        message.toByteArray(), msg -> {
                            msg.getMessageProperties().setDelay(delay);
                            msg.getMessageProperties().setHeader(B2BMQConstant.MESSAGE_PROPERTIES_HEADER_KEY_TIMES, times);
                            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return msg;
                        }, new CorrelationData());
                return null;
            }, context -> {
                //重试流程正常结束或者达到重试上限后
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQPraiseMessage.PraiseActionMessage msg = MQPraiseMessage.PraiseActionMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("delay send error,msg body:{}",msg,throwable);
                String msgJson = new JsonFormat().printToString(msg);
                LogUtils.saveLog("PraiseFormMessageSender","retry",msgJson,throwable,null);
                return null;
            });
        } catch (Exception e) {
            if(message == null) {
                log.error("[sendRetry] message is null", e);
                return;
            }
            String msgJson = new JsonFormat().printToString(message);
            log.error("delay send error,msg body:{}",msgJson,e);
            try {
                LogUtils.saveLog("PraiseFormMessageSender","retry",msgJson,e,null);
            }catch (Exception e1){
                log.error("保存日志失败,body:{}",msgJson,e1);
            }
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
