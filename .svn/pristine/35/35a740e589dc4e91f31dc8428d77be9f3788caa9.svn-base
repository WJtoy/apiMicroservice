package com.wolfking.jeesite.modules.mq.conf;

import com.kkl.kklplus.entity.b2b.mq.B2BMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于接收第三方系统回调的投诉处理进度
 * @date 2020-10-13
 */
@Configuration
public class B2BCenterOrderReminderCloseConfig extends CommonConfig {
    /**
     * 延时秒数
     */
    public static final int DELAY_MILLISECOND = 10 * 1000;
    /**
     * 重试次数
     */
    public static final int RETRY_TIMES = 3;

    /**
     * 默认用户Id
     */
    public static final Long B2B_UID = 3L;

    @Bean
    public Queue b2bCenterOrderReminderCloseQueue() {
        return new Queue(B2BMQConstant.MQ_B2BCENTER_CLOSE_B2BORDER_REMINDER_DELAY, true);
    }

    @Bean
    DirectExchange b2bCenterOrderReminderCloseExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(B2BMQConstant.MQ_B2BCENTER_CLOSE_B2BORDER_REMINDER_DELAY).
                delayed().withArgument("x-delayed-type", "direct").build();
    }

    @Bean
    Binding bindingB2BCenterOrderReminderCloseExchangeMessage(Queue b2bCenterOrderReminderCloseQueue, DirectExchange b2bCenterOrderReminderCloseExchange) {
        return BindingBuilder.bind(b2bCenterOrderReminderCloseQueue).
                to(b2bCenterOrderReminderCloseExchange).
                with(B2BMQConstant.MQ_B2BCENTER_CLOSE_B2BORDER_REMINDER_DELAY);
    }

   /* @Bean
    MessageListenerAdapter b2bCenterOrderReminderCloseListenerAdapter(B2BCenterOrderReminderCloseMQReceiver b2BCenterOrderReminderCloseMQReceiver) {
        return new MessageListenerAdapter(b2BCenterOrderReminderCloseMQReceiver);
    }
*/
   /* @Bean
    SimpleMessageListenerContainer b2bCenterOrderReminderCloseRetryContainer(MessageListenerAdapter b2bCenterOrderReminderCloseListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(manualConnectionFactory());
        container.setQueues(b2bCenterOrderReminderCloseQueue());
        container.setAcknowledgeMode(AcknowledgeMode.valueOf(acknowledgeMode.toUpperCase()));
        container.setConcurrentConsumers(concurrency);
        container.setMaxConcurrentConsumers(maxConcurrency);
        container.setPrefetchCount(prefetch);
        container.setMessageListener(b2bCenterOrderReminderCloseListenerAdapter);
        return container;
    }*/

}
