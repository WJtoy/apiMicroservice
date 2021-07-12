package com.wolfking.jeesite.modules.mq.conf.voice;

import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.wolfking.jeesite.common.config.WebProperties;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

/**
 * api接口回调消息队列
 */
@Configuration
public class CallbackMQConfig extends CommonConfig {

//    @Value("${site.code}")
    private String siteCode;

    @Autowired
    public void setWebProperties(WebProperties webProperties) {
        this.siteCode = webProperties.getSite().getCode();
    }

    //private final String QUEUE_NAME = MessageFormat.format("{0}:{1}", VoiceServiceMQConstant.MQ_VOICE_RECEIVE_CALLBACK,siteCode);

    @Bean
    public Queue callbackQueue() {
        return new Queue(MessageFormat.format("{0}:{1}", VoiceServiceMQConstant.MQ_VOICE_RECEIVE_CALLBACK,siteCode), true);
    }

    @Bean
    DirectExchange callbackExchange() {
        return new DirectExchange(MessageFormat.format("{0}:{1}", VoiceServiceMQConstant.MQ_VOICE_RECEIVE_CALLBACK,siteCode));
    }

    @Bean
    Binding bindingCallbackExchangeMessage(Queue callbackQueue, DirectExchange callbackExchange) {
        return BindingBuilder.bind(callbackQueue)
                .to(callbackExchange)
                .with(MessageFormat.format("{0}:{1}", VoiceServiceMQConstant.MQ_VOICE_RECEIVE_CALLBACK,siteCode));
    }

}
