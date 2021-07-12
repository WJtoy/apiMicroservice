package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 下单消息重试队列
 */
@Configuration
public class CreateOrderPushMessageRetryConfig extends CommonConfig {

    public static final String MQ_CREATEORDER_PUSH_MESSAGE_RETRY = "MQ:CREATEORDER:MESSAGE:RETRY";

    @Bean
    public Queue createOrderPushMessageRetryQueue() {
        return new Queue(MQ_CREATEORDER_PUSH_MESSAGE_RETRY, true);
    }

    @Bean
    DirectExchange createOrderPushMessageRetryExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQ_CREATEORDER_PUSH_MESSAGE_RETRY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingCreateOrderRetryExchangeMessage(Queue createOrderPushMessageRetryQueue, DirectExchange createOrderPushMessageRetryExchange) {
        return BindingBuilder.bind(createOrderPushMessageRetryQueue)
                .to(createOrderPushMessageRetryExchange)
                .with(MQ_CREATEORDER_PUSH_MESSAGE_RETRY);
    }
}
