package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单统计数据队列
 */
@Configuration
public class OrderReportConfig extends CommonConfig{

    public static final String MQ_ORDER_REPORT_EXCHANGE = "MQ:ORDER:REPORT:EXCHANGE";
    public static final String MQ_ORDER_REPORT_ROUTING = "MQ:ORDER:REPORT:ROUTING";
    public static final String MQ_ORDER_REPORT_QUEUE = "MQ:ORDER:REPORT:QUEUE";
    public static final String MQ_ORDER_REPORT_COUNTER  = "ORDERREPORT";

    @Bean
    public Queue orderReportQueue() {
        return new Queue(MQ_ORDER_REPORT_QUEUE, true);
    }

    @Bean
    DirectExchange orderReportExchange() {
        return new DirectExchange(MQ_ORDER_REPORT_EXCHANGE);
    }

    @Bean
    Binding bindingOrderReportExchangeMessage() {
        return BindingBuilder.bind(orderReportQueue()).to(orderReportExchange()).with(MQ_ORDER_REPORT_ROUTING);
    }
}
