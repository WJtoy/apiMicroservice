package com.wolfking.jeesite.ms.b2bcenter.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 天猫一键求助
 */
@Configuration
@ConditionalOnProperty(name = "ms.b2bcenter.mq.order.consumer.enabled", matchIfMissing = false)
public class B2BCenterAnomalyRecourseMQConfig extends CommonConfig {

    @Bean
    public Queue b2bCenterAnomalyRecourseQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_ANOMALYRECOURSE, true);
    }

    @Bean
    DirectExchange b2bCenterAnomalyRecourseExchange() {
        return new DirectExchange(B2BMQConstant.MQ_B2BCENTER_ANOMALYRECOURSE);
    }

    @Bean
    Binding bindingAnomalyRecourseExchangeMessage(Queue b2bCenterAnomalyRecourseQueue, DirectExchange b2bCenterAnomalyRecourseExchange) {
        return BindingBuilder.bind(b2bCenterAnomalyRecourseQueue)
                .to(b2bCenterAnomalyRecourseExchange)
                .with(B2BMQConstant.MQ_B2BCENTER_ANOMALYRECOURSE);
    }

}
