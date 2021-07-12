package com.wolfking.jeesite.ms.praise.mq.config;

import com.kkl.kklplus.entity.praise.mq.MQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 好评单异步消息队列配置
 *
 * @author Ryan Lu
 * @date 2020/03/31 10:59 AM
 * @since 1.0.0
 */
@Configuration
public class PraiseMQConfig extends CommonConfig {

    @Bean
    public Queue praiseFormQueue() {
        return new Queue(MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY, true);
    }

    @Bean
    DirectExchange praiseFormExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingPraiseFormExchangeMessage(Queue praiseFormQueue, DirectExchange praiseFormExchange) {
        return BindingBuilder.bind(praiseFormQueue)
                .to(praiseFormExchange)
                .with(MQConstant.MS_MQ_PRAISE_REVIEW_RESULT_DELAY);
    }

}
