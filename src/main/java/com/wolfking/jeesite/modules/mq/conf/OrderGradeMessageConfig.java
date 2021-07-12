package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单客评消息队列
 * 1.保存客评信息(sd_order_grade)
 * 2.更新网点及安维评分
 */
@Configuration
public class OrderGradeMessageConfig extends CommonConfig {

    public static final String MQ_ORDER_GRADE = "MQ:ORDER:GRADE";
    
    @Bean
    public Queue orderGradeMessageQueue() {
        return new Queue(MQ_ORDER_GRADE, true);
    }

    @Bean
    DirectExchange orderGradeMessageExchange() {
        return new DirectExchange(MQ_ORDER_GRADE);
    }

    @Bean
    Binding bindingOrderGradeMessageExchange(Queue orderGradeMessageQueue, DirectExchange orderGradeMessageExchange) {
        return BindingBuilder.bind(orderGradeMessageQueue)
                .to(orderGradeMessageExchange)
                .with(MQ_ORDER_GRADE);
    }

}
