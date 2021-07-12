package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单自动派单消息队列
 */
@Configuration
public class OrderAutoPlanRetryMessageConfig extends CommonConfig {

    public static final String MQ_ORDER_AUTO_PLAN = "MQ:ORDER:AUTO:PLAN";
    
    @Bean
    public Queue orderAutoPlanRetryMessageQueue() {
        return new Queue(MQ_ORDER_AUTO_PLAN, true);
    }

    @Bean
    DirectExchange orderAutoPlanRetryMessageExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQ_ORDER_AUTO_PLAN).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingOrderAutoPlanRetryMessageExchange(Queue orderAutoPlanRetryMessageQueue, DirectExchange orderAutoPlanRetryMessageExchange) {
        return BindingBuilder.bind(orderAutoPlanRetryMessageQueue)
                .to(orderAutoPlanRetryMessageExchange)
                .with(MQ_ORDER_AUTO_PLAN);
    }

}
