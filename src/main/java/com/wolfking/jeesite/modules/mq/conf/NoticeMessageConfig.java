package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoticeMessageConfig extends CommonConfig {

    //提醒消息
    public static final String MQ_NOTICE_EXCHANGE   = "MQ:MESSAGE:NOTICE:EXCHANGE";
    public static final String MQ_NOTICE_ROUTING    = "MQ:MESSAGE:NOTICE:ROUTING";
    public static final String MQ_NOTICE_QUEUE      = "MQ:MESSAGE:NOTICE:QUEUE";
    public static final String MQ_NOTICE_COUNTER    = "WEBNOTICE";

    public static final int NOTICE_TYPE_FEEDBACK = 1; // 未读问题反馈(未读消息)
    public static final int NOTICE_TYPE_FEEDBACK_PENDING = 2; // 待处理问题反馈（反馈未处理）
    public static final int NOTICE_TYPE_APPABNORMALY = 3; // app异常(异常反馈)

    @Bean
    public Queue noticeMessageQueue() {
        return new Queue(MQ_NOTICE_QUEUE, true);
    }

    @Bean
    DirectExchange noticeMessageExchange() {
        return new DirectExchange(MQ_NOTICE_EXCHANGE);
    }

    @Bean
    Binding wsMessageBindingExchangeMessage() {
        return BindingBuilder.bind(noticeMessageQueue()).to(noticeMessageExchange()).with(MQ_NOTICE_ROUTING);
    }
}
