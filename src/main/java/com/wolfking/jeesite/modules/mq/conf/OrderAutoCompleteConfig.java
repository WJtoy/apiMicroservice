package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * APP确认完成，自动客评及对账队列
 * Created by Ryan on 2017/12/05.
 */
@Configuration
public class OrderAutoCompleteConfig extends CommonConfig {

    public static final String MQ_ORDER_AUTOCOMPLETE_EXCHANGE = "MQ:ORDER:AUTOCOMPLETE:EXCHANGE";
    public static final String MQ_ORDER_AUTOCOMPLETE_ROUTING = "MQ:ORDER:AUTOCOMPLETE:ROUTING";
    public static final String MQ_ORDER_AUTOCOMPLETE_QUEUE = "MQ:ORDER:AUTOCOMPLETE:QUEUE";
    public static final String MQ_ORDER_AUTOCOMPLETE_COUNTER = "ORDERAUTOCOMPLETE";

    public static final String MQ_ORDER_AUTOCOMPLETE_DELAY = "MQ:ORDER:AUTOCOMPLETED:DELAY";

    public static final String MQ_DELAY_ARG_XDELAYEDTYPE_KEY = "x-delayed-type";
    public static final String MQ_DELAY_ARG_XDELAYEDTYPE_VALUE = "direct";

    public static final int DELAY_MILLISECOND = 10 * 1000;

    @Bean
    public Queue orderAutoCompleteQueue() {
        return new Queue(MQ_ORDER_AUTOCOMPLETE_QUEUE, true);
    }

    @Bean
    DirectExchange orderAutoCompleteExchange() {
        return new DirectExchange(MQ_ORDER_AUTOCOMPLETE_EXCHANGE);
    }

    @Bean
    Binding bindingOrderAutoCompleteExchangeMessage(Queue orderAutoCompleteQueue, DirectExchange orderAutoCompleteExchange) {
        return BindingBuilder.bind(orderAutoCompleteQueue).to(orderAutoCompleteExchange).with(MQ_ORDER_AUTOCOMPLETE_ROUTING);
    }

    //--------------------------------------------------delay queue-----------------------------------------------------

    @Bean
    public Queue orderAutoCompleteDelayQueue() {
        return new Queue(MQ_ORDER_AUTOCOMPLETE_DELAY, true);
    }

    @Bean
    DirectExchange orderAutoCompleteDelayExchange() {
        return (DirectExchange) ExchangeBuilder
                .directExchange(MQ_ORDER_AUTOCOMPLETE_DELAY)
                .delayed()
                .withArgument(MQ_DELAY_ARG_XDELAYEDTYPE_KEY, MQ_DELAY_ARG_XDELAYEDTYPE_VALUE)
                .build();
    }

    @Bean
    Binding bindingOrderAutoCompleteDelayExchangeMessage(Queue orderAutoCompleteDelayQueue, DirectExchange orderAutoCompleteDelayExchange) {
        return BindingBuilder
                .bind(orderAutoCompleteDelayQueue)
                .to(orderAutoCompleteDelayExchange)
                .with(MQ_ORDER_AUTOCOMPLETE_DELAY);
    }
}
