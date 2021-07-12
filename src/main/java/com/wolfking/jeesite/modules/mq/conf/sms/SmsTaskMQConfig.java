package com.wolfking.jeesite.modules.mq.conf.sms;

import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信回访消息队列
 */
@Configuration
public class SmsTaskMQConfig extends CommonConfig {

    //region

    @Bean
    public Queue newSmsTaskQueue() {
        return new Queue(VoiceServiceMQConstant.MQ_SMS_CALLBACK_TASK, true);
    }

    @Bean
    DirectExchange newSmsTaskExchange() {
        return new DirectExchange(VoiceServiceMQConstant.MQ_SMS_CALLBACK_TASK);
    }

    @Bean
    Binding bindingNewTaskExchangeMessage(Queue newSmsTaskQueue, DirectExchange newSmsTaskExchange) {
        return BindingBuilder.bind(newSmsTaskQueue)
                .to(newSmsTaskExchange)
                .with(VoiceServiceMQConstant.MQ_SMS_CALLBACK_TASK);
    }

    //endregion

}
