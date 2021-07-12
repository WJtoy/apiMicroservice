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
public class B2BCenterNewB2BOrderReminderMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterNewB2BOrderReminderQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_NEW_B2BORDER_REMINDER, true);
    }

    @Bean
    DirectExchange b2bCenterNewB2BOrderReminderExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_NEW_B2BORDER_REMINDER);
    }

    @Bean
    Binding bindingB2BCenterNewB2BOrderReminderExchangeMessage(Queue b2bCenterNewB2BOrderReminderQueue, DirectExchange b2bCenterNewB2BOrderReminderExchange) {
        return BindingBuilder.bind(b2bCenterNewB2BOrderReminderQueue)
                .to(b2bCenterNewB2BOrderReminderExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_NEW_B2BORDER_REMINDER);
    }

}
