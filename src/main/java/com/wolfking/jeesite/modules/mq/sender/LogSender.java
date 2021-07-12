package com.wolfking.jeesite.modules.mq.sender;

import com.kkl.kklplus.entity.sys.mq.MQSysLogMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: JeffZhao
 * @Date: 2018/7/12 11:22
 * @Description
 */
@Component
public class LogSender{

    private RabbitTemplate logRabbitTemplate;

    @Autowired
    LogSender(RabbitTemplate manualRabbitTemplate){
        this.logRabbitTemplate = manualRabbitTemplate;
    }

    public void send(MQSysLogMessage.SysLogMessage logMessage){
        this.logRabbitTemplate.convertAndSend(
                com.kkl.kklplus.entity.sys.mq.MQConstant.MS_MQ_SYS_LOG,
                com.kkl.kklplus.entity.sys.mq.MQConstant.MS_MQ_SYS_LOG,
                logMessage.toByteArray(),
                new CorrelationData(UUID.randomUUID().toString())
        );
    }
}
