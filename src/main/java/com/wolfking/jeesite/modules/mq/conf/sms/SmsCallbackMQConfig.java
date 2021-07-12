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
 * 短信回访回调消息队列
 */
@Configuration
public class SmsCallbackMQConfig extends CommonConfig {

//    @Value("${site.code}")
//    private String siteCode;

    @Bean
    public Queue smsCallbackQueue() {
        return new Queue(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK, true);
    }

    @Bean
    DirectExchange smsCallbackExchange() {
        return new DirectExchange(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK);
    }

    @Bean
    Binding bindingSmsCallbackExchangeMessage(Queue smsCallbackQueue, DirectExchange smsCallbackExchange) {
        return BindingBuilder.bind(smsCallbackQueue)
                .to(smsCallbackExchange)
                .with(VoiceServiceMQConstant.MQ_SMS_RECEIVE_CALLBACK);
    }
}
