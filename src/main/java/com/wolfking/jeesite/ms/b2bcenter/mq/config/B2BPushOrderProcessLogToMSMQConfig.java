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
public class B2BPushOrderProcessLogToMSMQConfig extends CommonConfig {

    @Bean
    public Queue b2bPushOrderProcessLogToMSQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_PUSH_ORDERPROCESSLOG_TO_MS, true);
    }

    @Bean
    DirectExchange b2bPushOrderProcessLogToMSExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_PUSH_ORDERPROCESSLOG_TO_MS);
    }

    @Bean
    Binding bindingB2BPushOrderProcessLogToMSExchangeMessage(Queue b2bPushOrderProcessLogToMSQueue, DirectExchange b2bPushOrderProcessLogToMSExchange) {
        return BindingBuilder.bind(b2bPushOrderProcessLogToMSQueue)
                .to(b2bPushOrderProcessLogToMSExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_PUSH_ORDERPROCESSLOG_TO_MS);
    }

}
