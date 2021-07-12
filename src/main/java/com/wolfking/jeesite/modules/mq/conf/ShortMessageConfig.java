package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortMessageConfig extends CommonConfig {

    //短信发送队列
    public static final String MQ_SHORTMESSAGE_EXCHANGE = "MQ:MESSAGE:SHORTMESSAGE:EXCHANGE";
    public static final String MQ_SHORTMESSAGE_ROUTING = "MQ:MESSAGE:SHORTMESSAGE:ROUTING";
    public static final String MQ_SHORTMESSAGE_QUEUE = "MQ:MESSAGE:SHORTMESSAGE:QUEUE";
    public static final String MQ_SHORTMESSAGE_COUNTER  = "SHORTMESSAGE";

    @Bean
    public Queue shortMessageQueue() {
        return new Queue(MQ_SHORTMESSAGE_QUEUE, true);
    }

    @Bean
    DirectExchange shortMessageExchange() {
        return new DirectExchange(MQ_SHORTMESSAGE_EXCHANGE);
    }

    @Bean
    Binding shortMessageBindingExchangeMessage() {
        return BindingBuilder.bind(shortMessageQueue()).to(shortMessageExchange()).with(MQ_SHORTMESSAGE_ROUTING);
    }



}
