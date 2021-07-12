package com.wolfking.jeesite.modules.mq.conf.sms;

import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 非客评短信回复消息队列
 */
@Configuration
public class SmsCallbackNoGradeMQConfig extends CommonConfig {

//    @Value("${site.code}")
//    private String siteCode;

    @Bean
    public Queue smsCallbackNoGradeQueue() {
        return new Queue(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK_NOGRADE, true);
    }

    @Bean
    DirectExchange smsCallbackNoGradeExchange() {
        return new DirectExchange(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK_NOGRADE);
    }

    @Bean
    Binding bindingSmsCallbackExchangeMessage(Queue smsCallbackNoGradeQueue, DirectExchange smsCallbackNoGradeExchange) {
        return BindingBuilder.bind(smsCallbackNoGradeQueue)
                .to(smsCallbackNoGradeExchange)
                .with(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK_NOGRADE);
    }
}
