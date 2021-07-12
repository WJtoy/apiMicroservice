package com.wolfking.jeesite.modules.mq.conf;

import com.kkl.kklplus.entity.push.PushConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将推送切换为微服务
 */
@Configuration
public class PushMessageConfig extends CommonConfig {

    @Bean
    public Queue pushMessageQueue() {
        return new Queue(PushConstant.MQ_PUSH_MESSAGE_QUEUE, true);
    }

    @Bean
    DirectExchange pushMessageExchange() {
        return new DirectExchange(PushConstant.MQ_PUSH_MESSAGE_EXCHANGE);
    }

    @Bean
    Binding bindingPushMessageExchangeMessage() {
        return BindingBuilder.bind(pushMessageQueue()).to(pushMessageExchange()).with(PushConstant.MQ_PUSH_MESSAGE_ROUTING);
    }
}
