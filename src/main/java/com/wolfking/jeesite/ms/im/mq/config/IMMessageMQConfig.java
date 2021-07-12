package com.wolfking.jeesite.ms.im.mq.config;

import com.kkl.kklplus.entity.sys.mq.MQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *站内即时通知队列
 */
@Configuration
public class IMMessageMQConfig extends CommonConfig {

    @Bean
    public Queue imMessageQueue() {
        return new Queue(MQConstant.MS_MQ_IM, true);
    }

    @Bean
    DirectExchange imMessageExchange() {
        return new DirectExchange(MQConstant.MS_MQ_IM);
    }

    @Bean
    Binding bindingSystemNoticeExchangeMessage(Queue imMessageQueue, DirectExchange imMessageExchange) {
        return BindingBuilder.bind(imMessageQueue)
                .to(imMessageExchange)
                .with(MQConstant.MS_MQ_IM);
    }

}
