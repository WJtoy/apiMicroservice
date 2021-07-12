package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单网点消息队列
 */
@Configuration
public class ServicePointOrderRetryMessageConfig extends CommonConfig {

    public static final String MQ_SERVICEPOINT_ORDER_RETRY = "MQ:SERVICEPOINT:ORDER:RETRY";
    
    @Bean
    public Queue servicePointOrderRetryMessageQueue() {
        return new Queue(MQ_SERVICEPOINT_ORDER_RETRY, true);
    }

    @Bean
    DirectExchange servicePointOrderRetryMessageExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQ_SERVICEPOINT_ORDER_RETRY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingOrderGradeMessageExchange(Queue servicePointOrderRetryMessageQueue, DirectExchange servicePointOrderRetryMessageExchange) {
        return BindingBuilder.bind(servicePointOrderRetryMessageQueue)
                .to(servicePointOrderRetryMessageExchange)
                .with(MQ_SERVICEPOINT_ORDER_RETRY);
    }

}
