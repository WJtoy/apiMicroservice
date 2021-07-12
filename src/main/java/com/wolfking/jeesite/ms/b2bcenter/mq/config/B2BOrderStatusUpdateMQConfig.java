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
public class B2BOrderStatusUpdateMQConfig extends CommonConfig {

    @Bean
    public Queue b2bOrderStatusUpdateQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_B2BORDER_STATUS_UPDATE, true);
    }

    @Bean
    DirectExchange b2bOrderStatusUpdateExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_B2BORDER_STATUS_UPDATE);
    }

    @Bean
    Binding bindingB2BOrderStatusUpdateExchangeMessage(Queue b2bOrderStatusUpdateQueue, DirectExchange b2bOrderStatusUpdateExchange) {
        return BindingBuilder.bind(b2bOrderStatusUpdateQueue)
                .to(b2bOrderStatusUpdateExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_B2BORDER_STATUS_UPDATE);
    }

}
