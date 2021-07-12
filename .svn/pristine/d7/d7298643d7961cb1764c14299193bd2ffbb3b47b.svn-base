package com.wolfking.jeesite.modules.mq.conf;

import com.kkl.kklplus.entity.rpt.mq.RPTMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 每日下单消息队列(用于报表微服务统计)
 */
@Configuration
public class RPTCreatedOrderMessageConfig extends CommonConfig {

    @Bean
    public Queue createdOrderMessageQueue() {
        return new Queue(RPTMQConstant.MQ_RPT_CREATE_ORDER, true);
    }

    @Bean
    DirectExchange createdOrderMessageExchange() {
        return new DirectExchange(RPTMQConstant.MQ_RPT_CREATE_ORDER);
    }

    @Bean
    Binding bindingCreatedOrderMessageExchange(Queue createdOrderMessageQueue, DirectExchange createdOrderMessageExchange) {
        return BindingBuilder.bind(createdOrderMessageQueue)
                .to(createdOrderMessageExchange)
                .with(RPTMQConstant.MQ_RPT_CREATE_ORDER);
    }

}
