package com.worldelite.job.service;

import com.worldelite.job.entity.Message;
import com.worldelite.job.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 发送站内消息
     * @param message
     */
    public void sendMessage(Message message){
        messageMapper.insertSelective(message);
    }

}
