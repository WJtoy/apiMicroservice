package com.wolfking.jeesite.modules.mq.conf;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Created by Jeff on 2017/8/2.
 */
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class CommonConfig {
//    @Value("${spring.rabbitmq.host}")
    private String host;
//    @Value("${spring.rabbitmq.port}")
    private Integer port;
//    @Value("${spring.rabbitmq.username}")
    private String userName;
//    @Value("${spring.rabbitmq.password}")
    private String password;
//    @Value("${spring.rabbitmq.publisherConfirms}")
    private boolean publisherConfirms;
//    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    protected String acknowledgeMode;
//    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    protected int concurrency;
//    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    protected int maxConcurrency;
//    @Value("${spring.rabbitmq.listener.simple.prefetch}")
    protected int prefetch;

    @Autowired
    public void setRabbitProperties(RabbitProperties rabbitProperties) {
        this.host = rabbitProperties.getHost();
        this.port = rabbitProperties.getPort();
        this.userName = rabbitProperties.getUsername();
        this.password = rabbitProperties.getPassword();
        this.publisherConfirms = rabbitProperties.isPublisherConfirms();
    }

    @Bean
    public ConnectionFactory manualConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(publisherConfirms); //必须要设置
        return connectionFactory;
    }

    /**
     * TODO 全部MQ替换之后去掉，引用kklplus-starter-rabbit
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate manualRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(manualConnectionFactory());
        return rabbitTemplate;
    }

    /**
     * TODO 全部MQ替换之后去掉，引用kklplus-starter-rabbit
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RetryTemplate kklRabbitRetryTemplate(RabbitProperties rabbitProperties) {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setMaxInterval(rabbitProperties.getTemplate().getRetry().getMaxInterval());
        backOffPolicy.setInitialInterval(rabbitProperties.getTemplate().getRetry().getInitialInterval());
        backOffPolicy.setMultiplier(rabbitProperties.getTemplate().getRetry().getMultiplier());
        retryTemplate.setBackOffPolicy(backOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(rabbitProperties.getTemplate().getRetry().getMaxAttempts());
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

}
