package com.wolfking.jeesite.ms.b2bcenter.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class B2BAsyncTriggerB2BOperationMQConfig extends CommonConfig {

    public static final int DELAY_MILLISECOND = 1 * 60 * 1000;

    @Bean
    public Queue b2bAsyncTriggerB2BOperationQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_ASYNC_TRIGGER_B2B_OPERATION_DELAY, true);
    }

    @Bean
    DirectExchange b2bAsyncTriggerB2BOperationExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(B2BMQConstant.MQ_B2BCENTER_ASYNC_TRIGGER_B2B_OPERATION_DELAY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }


    @Bean
    Binding bindingB2BAsyncTriggerB2BOperationExchangeMessage(Queue b2bAsyncTriggerB2BOperationQueue, DirectExchange b2bAsyncTriggerB2BOperationExchange) {
        return BindingBuilder.bind(b2bAsyncTriggerB2BOperationQueue)
                .to(b2bAsyncTriggerB2BOperationExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_ASYNC_TRIGGER_B2B_OPERATION_DELAY);
    }

}
