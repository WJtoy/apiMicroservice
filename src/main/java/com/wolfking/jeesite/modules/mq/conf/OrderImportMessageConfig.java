package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 人工导入订单处理消息队列
 * 1.订单检查(包含基本信息、区域)
 * 2.自动保存订单，会分发其他报表消息
 *
 * 注：发生错误保存在sd_temporder表中
 */
@Configuration
public class OrderImportMessageConfig extends CommonConfig {

    public static final String MQ_ORDER_IMPORT = "MQ:ORDER:IMPORT";
    
    @Bean
    public Queue orderImportMessageQueue() {
        return new Queue(MQ_ORDER_IMPORT, true);
    }

    @Bean
    DirectExchange orderImportMessageExchange() {
        return new DirectExchange(MQ_ORDER_IMPORT);
    }

    @Bean
    Binding bindingOrderImportMessageExchange(Queue orderImportMessageQueue, DirectExchange orderImportMessageExchange) {
        return BindingBuilder.bind(orderImportMessageQueue)
                .to(orderImportMessageExchange)
                .with(MQ_ORDER_IMPORT);
    }

}
