package com.wolfking.jeesite.modules.mq.conf.voice;

import com.kkl.kklplus.entity.voiceservice.mq.VoiceServiceMQConstant;
import com.wolfking.jeesite.modules.mq.conf.CommonConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新营销任务消息队列
 */
@Configuration
public class NewTaskMQConfig extends CommonConfig {

    //region

    @Bean
    public Queue newTaskQueue() {
        return new Queue(VoiceServiceMQConstant.MQ_VOICE_SEND_NEW_TASK, true);
    }

    @Bean
    DirectExchange newTaskExchange() {
        return new DirectExchange(VoiceServiceMQConstant.MQ_VOICE_SEND_NEW_TASK);
    }

    @Bean
    Binding bindingNewTaskExchangeMessage(Queue newTaskQueue, DirectExchange newTaskExchange) {
        return BindingBuilder.bind(newTaskQueue)
                .to(newTaskExchange)
                .with(VoiceServiceMQConstant.MQ_VOICE_SEND_NEW_TASK);
    }

    //endregion

}
