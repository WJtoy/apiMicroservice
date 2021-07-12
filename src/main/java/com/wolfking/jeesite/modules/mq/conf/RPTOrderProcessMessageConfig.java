package com.wolfking.jeesite.modules.mq.conf;

import com.kkl.kklplus.entity.rpt.mq.RPTMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 报表微服务消息队列(下单,取消单,退单,客评)
 */
@Configuration
public class RPTOrderProcessMessageConfig extends CommonConfig {

    @Bean
    public Queue rptOrderProcessQueue() {
        return new Queue(RPTMQConstant.MQ_RPT_ORDER_PROCESS_DELAY, true);
    }

    @Bean
    DirectExchange rptOrderProcessExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(RPTMQConstant.MQ_RPT_ORDER_PROCESS_DELAY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingRPTOrderProcessExchangeMessage(Queue rptOrderProcessQueue, DirectExchange rptOrderProcessExchange) {
        return BindingBuilder.bind(rptOrderProcessQueue)
                .to(rptOrderProcessExchange)
                .with(RPTMQConstant.MQ_RPT_ORDER_PROCESS_DELAY);
    }
}
