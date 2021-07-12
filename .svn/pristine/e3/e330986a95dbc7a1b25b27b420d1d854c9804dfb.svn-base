package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderChargeConfig extends CommonConfig {

    //订单自动对账结账队列
    public static final String MQ_ORDER_CHARGE = "MQ:ORDER:CHARGE";
    public static final String MQ_ORDER_CHARGE_COUNTER  = "FI.OrderCharge";

    @Bean
    public Queue orderChargeQueue() {
        return new Queue(MQ_ORDER_CHARGE, true);
    }

    @Bean
    DirectExchange orderChargeExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQ_ORDER_CHARGE).delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingOrderChargeExchangeMessage(Queue orderChargeQueue, DirectExchange orderChargeExchange) {
        return BindingBuilder.bind(orderChargeQueue).to(orderChargeExchange).with(MQ_ORDER_CHARGE);
    }

}
