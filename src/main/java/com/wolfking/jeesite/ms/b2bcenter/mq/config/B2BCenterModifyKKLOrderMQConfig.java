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
public class B2BCenterModifyKKLOrderMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterModifyKKLOrderQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_MODIFY_KKL_ORDER, true);
    }

    @Bean
    DirectExchange b2bCenterModifyKKLOrderExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_MODIFY_KKL_ORDER);
    }

    @Bean
    Binding bindingB2BCenterModifyKKLOrderExchangeMessage(Queue b2bCenterModifyKKLOrderQueue, DirectExchange b2bCenterModifyKKLOrderExchange) {
        return BindingBuilder.bind(b2bCenterModifyKKLOrderQueue)
                .to(b2bCenterModifyKKLOrderExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_MODIFY_KKL_ORDER);
    }

}
