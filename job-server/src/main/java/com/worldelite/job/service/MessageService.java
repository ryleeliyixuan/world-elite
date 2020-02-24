package com.worldelite.job.service;

import com.worldelite.job.entity.Message;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.MessageMapper;
import com.worldelite.job.vo.MessageVo;
import com.worldelite.job.vo.PageResult;
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

    /**
     * 获取用户消息列表
     *
     * @param pageForm
     * @return
     */
    public PageResult<MessageVo> getUserMessageList(PageForm pageForm){
        return null;
    }

    /**
     * 获取当前用户未读消息数
     *
     * @return
     */
    public Integer getUserUnreadCount(){
        return null;
    }

    /**
     * 标识单条消息已读
     *
     * @param id
     */
    public void markMessageRead(Integer id){

    }

    /**
     * 标识所有消息已读
     */
    public void markAllRead(){

    }
}
