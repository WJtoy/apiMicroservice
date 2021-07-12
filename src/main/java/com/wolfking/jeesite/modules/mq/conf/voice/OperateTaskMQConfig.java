package com.wolfking.jeesite.modules.mq.conf.voice;

import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 营销任务处理重试消息队列
 */
@Configuration
public class OperateTaskMQConfig extends CommonConfig {


    //region

    @Bean
    public Queue operateTaskQueue() {
        return new Queue(VoiceServiceMQConstant.MQ_VOICE_SEND_OPERATE_TASK, true);
    }

    @Bean
    DirectExchange operateTaskExchange() {
        return new DirectExchange(VoiceServiceMQConstant.MQ_VOICE_SEND_OPERATE_TASK);
    }

    @Bean
    Binding bindingOperateExchangeMessage(Queue operateTaskQueue, DirectExchange operateTaskExchange) {
        return BindingBuilder.bind(operateTaskQueue)
                .to(operateTaskExchange)
                .with(VoiceServiceMQConstant.MQ_VOICE_SEND_OPERATE_TASK);
    }

    //endregion

}
