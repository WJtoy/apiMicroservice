package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 人工导入订单重试消息队列
 */
@Configuration
public class OrderImportRetryMessageConfig extends CommonConfig {

    public static final String MQ_ORDER_IMPORT_RETRY = "MQ:ORDER:IMPORT:RETRY";

    /**
     * 重试的延迟时间
     */
    public static final int DELAY_MILLISECOND = 15 * 1000;
    /**
     * 重试次数
     */
    public static final int RETRY_TIMES = 3;

    @Bean
    public Queue orderImportMessageRetryQueue() {
        return new Queue(MQ_ORDER_IMPORT_RETRY, true);
    }

    @Bean
    DirectExchange orderImportMessageRetryExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQ_ORDER_IMPORT_RETRY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingOrderImportMessageRetryExchangeMessage(Queue orderImportMessageRetryQueue, DirectExchange orderImportMessageRetryExchange) {
        return BindingBuilder.bind(orderImportMessageRetryQueue)
                .to(orderImportMessageRetryExchange)
                .with(MQ_ORDER_IMPORT_RETRY);
    }

}
