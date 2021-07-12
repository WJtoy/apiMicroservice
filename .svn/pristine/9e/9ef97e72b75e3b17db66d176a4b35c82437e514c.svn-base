package com.wolfking.jeesite.ms.material.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * B2B物流消息队列
 * 包含：审核/驳回，发货通知
 */
@Configuration
public class B2BMaterialRetryMQConfig extends CommonConfig{


    @Bean
    public Queue b2BMaterialRetryQueue() {
        return new Queue(B2BMQConstant.MQ_B2B_MATERIAL_STATUS_NOTIFY_RETRY, true);
    }

    @Bean
    DirectExchange b2BMaterialRetryExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(B2BMQConstant.MQ_B2B_MATERIAL_STATUS_NOTIFY_RETRY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding b2BMaterialExchangeMessage(Queue b2BMaterialRetryQueue, DirectExchange b2BMaterialRetryExchange) {
        return BindingBuilder.bind(b2BMaterialRetryQueue)
                .to(b2BMaterialRetryExchange)
                .with(B2BMQConstant.MQ_B2B_MATERIAL_STATUS_NOTIFY_RETRY);
    }
}
