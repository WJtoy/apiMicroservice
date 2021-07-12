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
public class B2BCenterProcessKKLOrderMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterProcessKKLOrderQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_PROCESS_KKL_ORDER, true);
    }

    @Bean
    DirectExchange b2bCenterProcessKKLOrderExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_PROCESS_KKL_ORDER);
    }

    @Bean
    Binding bindingB2BCenterProcessKKLOrderExchangeMessage(Queue b2bCenterProcessKKLOrderQueue, DirectExchange b2bCenterProcessKKLOrderExchange) {
        return BindingBuilder.bind(b2bCenterProcessKKLOrderQueue)
                .to(b2bCenterProcessKKLOrderExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_PROCESS_KKL_ORDER);
    }

}
