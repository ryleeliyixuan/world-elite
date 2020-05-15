package com.worldelite.job.mq;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.context.MessageTopic;
import com.worldelite.job.service.MessageService;
import com.worldelite.job.vo.JobVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Component
@Slf4j
public class MessageTaskHandler implements MessageListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RedisSerializer<String> valueSerializer = stringRedisTemplate.getStringSerializer();
        String deserialize = valueSerializer.deserialize(message.getBody());
        JobMessage jobMessage = JSON.parseObject(deserialize, JobMessage.class);
        if(jobMessage != null){
            handleSendJobSubscribeMessage(jobMessage.getJob());
        }
    }

    /**
     * 发送消息导出 excel 消息
     *
     * @param message
     */
    public void sendJobMessage(JobMessage message) {
        stringRedisTemplate.convertAndSend(MessageTopic.TOPIC_SEND_MESSAGE, JSON.toJSONString(message));
    }

    /**
     * 处理职位订阅消息
     */
    private void handleSendJobSubscribeMessage(JobVo jobVo){
        messageService.sendJobSubscribeMessage(jobVo);
    }
}
