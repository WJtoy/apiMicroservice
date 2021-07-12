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
public class B2BCenterModifyB2BOrderMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterModifyB2BOrderQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_MODIFY_B2B_ORDER, true);
    }

    @Bean
    DirectExchange b2bCenterModifyB2BOrderExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_MODIFY_B2B_ORDER);
    }

    @Bean
    Binding bindingB2BCenterModifyB2BOrderExchangeMessage(Queue b2bCenterModifyB2BOrderQueue, DirectExchange b2bCenterModifyB2BOrderExchange) {
        return BindingBuilder.bind(b2bCenterModifyB2BOrderQueue)
                .to(b2bCenterModifyB2BOrderExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_MODIFY_B2B_ORDER);
    }

}
