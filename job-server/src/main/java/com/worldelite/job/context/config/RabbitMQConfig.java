package com.worldelite.job.context.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置
 **/
@Data
@Configuration
public class RabbitMQConfig {

    /**
     * lucene索引更新交换机
     */
    @Value("${lucene.index.cmd.exchange.name:index_cmd_update_exchange}")
    private String luceneExchangeName;
    /**
     * lucene索引更新队列
     */
    @Value("${lucene.index.cmd.queue.name}")
    private String luceneQueueName;

    /**
     * 定义消息转换器
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY));
    }

    /**
     * 定义消息模板用于发布消息，并且设置其消息转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jsonMessageConverter());
        factory.setConcurrentConsumers(Runtime.getRuntime().availableProcessors());
        factory.setMaxConcurrentConsumers(Runtime.getRuntime().availableProcessors() * 2);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    //region LUCENE索引更新
    @Bean
    public Queue luceneIndexCmdQueue() {
        //如果没有设置队列名称则默认为匿名队列
        if (StringUtils.isBlank(luceneQueueName)) {
            return new AnonymousQueue();
        }
        return new Queue(luceneQueueName, true);
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * <p>
     * <p>
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingKey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingKey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public FanoutExchange luceneIndexCmdFanoutExchange() {
        return new FanoutExchange(luceneExchangeName);
    }

    @Bean
    public Binding luceneIndexCmdBinding() {
        return BindingBuilder.bind(luceneIndexCmdQueue()).to(luceneIndexCmdFanoutExchange());
    }
    //endregion
}
