package com.wolfking.jeesite.ms.um.mq.config;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UMPushOrderWebToMSMQConfig extends CommonConfig {

    @Bean
    public Queue umPushOrderWebToMSQueue() {
        return new Queue(B2BMQConstant.MQ_UM_PUSH_ORDER_WEB_TO_MS, true);
    }

    @Bean
    public DirectExchange umPushOrderWebToMSExchange() {
        return new DirectExchange(B2BMQConstant.MQ_UM_PUSH_ORDER_WEB_TO_MS);
    }

    @Bean
    public Binding bindingUmPushOrderWebToMSExchange(Queue umPushOrderWebToMSQueue, DirectExchange umPushOrderWebToMSExchange) {
        return BindingBuilder.bind(umPushOrderWebToMSQueue).to(umPushOrderWebToMSExchange).with(B2BMQConstant.MQ_UM_PUSH_ORDER_WEB_TO_MS);
    }
}
