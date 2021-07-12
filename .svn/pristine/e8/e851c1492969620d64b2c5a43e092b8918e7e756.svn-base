package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 下单消息队列
 */
@Configuration
public class CreateOrderPushMessageConfig extends CommonConfig {

    //APP消息推送队列
    public static final String MQ_CREATEORDER_PUSH_MESSAGE_EXCHANGE = "MQ:CREATEORDER:MESSAGE:EXCHANGE";
    public static final String MQ_CREATEORDER_PUSH_MESSAGE_ROUTING = "MQ:CREATEORDER:MESSAGE:ROUTING";
    public static final String MQ_CREATEORDER_PUSH_MESSAGE_QUEUE = "MQ:CREATEORDER:MESSAGE:QUEUE";
    public static final String MQ_CREATEORDER_PUSH_MESSAGE_COUNTER  = "CREATEORDERPUSHMESSAGE";
    public static final String MQ_PUSH_MESSAGE_ANDROID_COUNTER = "ANDROID";
    public static final String MQ_PUSH_MESSAGE_IOS_COUNTER = "IOS";

    @Bean
    public Queue createOrderPushMessageQueue() {
        return new Queue(MQ_CREATEORDER_PUSH_MESSAGE_QUEUE, true);
    }

    @Bean
    DirectExchange createOrderPushMessageExchange() {
        return new DirectExchange(MQ_CREATEORDER_PUSH_MESSAGE_EXCHANGE);
    }

    @Bean
    Binding bindingCreateOrderPushMessageExchangeMessage() {
        return BindingBuilder.bind(createOrderPushMessageQueue()).to(createOrderPushMessageExchange()).with(MQ_CREATEORDER_PUSH_MESSAGE_ROUTING);
    }
}
