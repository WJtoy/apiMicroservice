package com.wolfking.jeesite.ms.b2bcenter.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class B2BCenterPushOrderInfoToMsMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterPushOrderInfoToMsQueue() {
        return new Queue(B2BMQConstant.MQ_B2B_CENTER_PUSH_ORDER_INFO_TO_MS, true);
    }

    @Bean
    DirectExchange b2bCenterPushOrderInfoToMsExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2B_CENTER_PUSH_ORDER_INFO_TO_MS);
    }

    @Bean
    Binding bindingB2BCenterPushOrderInfoToMsExchangeMessage(Queue b2bCenterPushOrderInfoToMsQueue, DirectExchange b2bCenterPushOrderInfoToMsExchange) {
        return BindingBuilder.bind(b2bCenterPushOrderInfoToMsQueue)
                .to(b2bCenterPushOrderInfoToMsExchange)
                .with(B2BMQConstant.MQ_B2B_CENTER_PUSH_ORDER_INFO_TO_MS);
    }

}
