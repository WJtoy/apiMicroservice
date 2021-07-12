package com.wolfking.jeesite.modules.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: Zhoucy
 * @date: 2020/12/10
 * @Description:
 */
@Configuration
public class ScheduledThreadPoolExecutorConfig {

    @Bean
    public ScheduledExecutorService orderDelayProcessExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }

}
