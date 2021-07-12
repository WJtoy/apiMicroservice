package com.wolfking.jeesite.ms.b2bcenter.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 天猫预警
 */
@Configuration
@ConditionalOnProperty(name = "ms.b2bcenter.mq.order.consumer.enabled", matchIfMissing = false)
public class B2BCenterServiceMonitorMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterServiceMonitorQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_SERVICEMONITOR, true);
    }

    @Bean
    DirectExchange b2bCenterServiceMonitorExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_SERVICEMONITOR);
    }

    @Bean
    Binding bindingServiceMonitorExchangeMessage(Queue b2bCenterServiceMonitorQueue, DirectExchange b2bCenterServiceMonitorExchange) {
        return BindingBuilder.bind(b2bCenterServiceMonitorQueue)
                .to(b2bCenterServiceMonitorExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_SERVICEMONITOR);
    }

}
