package com.wolfking.jeesite.ms.b2bcenter.mq.sender;

import com.googlecode.protobuf.format.JsonFormat;
import com.kkl.kklplus.entity.b2b.common.B2BProcessFlag;
import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.kkl.kklplus.entity.b2b.mq.B2BMQQueueType;
import com.kkl.kklplus.entity.b2b.pb.MQTmallAnomalyRecourseMessage;
import com.kkl.kklplus.entity.b2bcenter.pb.MQB2BOrderMessage;
import com.wolfking.jeesite.common.utils.Exceptions;
import com.wolfking.jeesite.ms.tmall.mq.service.MqB2bTmallLogService;
import com.wolfking.jeesite.ms.utils.B2BFailureLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * 天猫一键求助消息队列生产者
 */
@Slf4j
@Component
public class B2BCenterAnomalyRecourseMQSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private MqB2bTmallLogService mqB2bTmallLogService;

    private RabbitTemplate rabbitTemplate;
    private RetryTemplate retryTemplate;

    @Autowired
    public B2BCenterAnomalyRecourseMQSender(RabbitTemplate manualRabbitTemplate, RetryTemplate kklRabbitRetryTemplate) {
        this.rabbitTemplate = manualRabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.retryTemplate = kklRabbitRetryTemplate;
    }

    /**
     * 正常发送消息
     *
     * @param message 消息体
     */
    public void send(MQTmallAnomalyRecourseMessage.TmallAnomalyRecourseMessage message) {
        try {
            retryTemplate.execute((RetryCallback<Object, Exception>) context -> {
                context.setAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE, message);
                rabbitTemplate.convertAndSend(
                        B2BMQConstant.MQ_B2BCENTER_ANOMALYRECOURSE,
                        B2BMQConstant.MQ_B2BCENTER_ANOMALYRECOURSE,
                        message.toByteArray(),
                        new CorrelationData());
                return null;
            }, context -> {
                Object msgObj = context.getAttribute(B2BMQConstant.RETRY_CONTEXT_ATTRIBUTE_KEY_MESSAGE);
                MQTmallAnomalyRecourseMessage.TmallAnomalyRecourseMessage msg = MQTmallAnomalyRecourseMessage.TmallAnomalyRecourseMessage.parseFrom((byte[]) msgObj);
                Throwable throwable = context.getLastThrowable();
                log.error("normal send error {}, {}", throwable.getLocalizedMessage(), msg);
                String msgJson = new JsonFormat().printToString(msg);
                B2BFailureLogUtils.saveFailureLog(B2BMQQueueType.TMALLPUSH_ANOMALYRECOURSE, msgJson, 31L,
                        B2BProcessFlag.PROCESS_FLAG_FAILURE, 0,
                        "normal send error -- " + throwable.getLocalizedMessage());
                return null;
            });
        } catch (Exception e) {
            String msgJson = new JsonFormat().printToString(message);
            B2BFailureLogUtils.saveFailureLog(B2BMQQueueType.TMALLPUSH_ANOMALYRECOURSE, msgJson, 3L,
                    B2BProcessFlag.PROCESS_FLAG_FAILURE, 0, "normal send error -- " + e.getLocalizedMessage());
        }
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

    }
}
