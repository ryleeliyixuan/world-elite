package com.worldelite.job.context.config;


import com.worldelite.job.context.MessageTopic;
import com.worldelite.job.mq.ExportTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 消息订阅者配置类
 *
 * @author yeguozhong yedaxia.github.com
 */
@Configuration
public class SubscriberConfig {

    @Autowired
    private ExportTaskHandler exportExcelTaskHandler;

    /**
     * 创建消息监听容器
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(exportExcelTaskHandler, new PatternTopic(MessageTopic.TOPIC_EXPORT_EXCEL));
        return redisMessageListenerContainer;
    }

}
