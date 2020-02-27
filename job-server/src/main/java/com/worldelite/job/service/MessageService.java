package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.entity.Message;
import com.worldelite.job.form.MessageListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.MessageMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.MessageVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class MessageService extends BaseService{

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
     * @param listForm
     * @return
     */
    public PageResult<MessageVo> getUserMessageList(MessageListForm listForm){
        if(StringUtils.isEmpty(listForm.getSort())){
            listForm.setSort("id desc");
        }
        AppUtils.setPage(listForm);
        Message options = new Message();
        options.setToUser(curUser().getId());
        options.setReadFlag(listForm.getReadFlag());
        Page<Message> messagePage = (Page<Message>) messageMapper.selectAndList(options);
        PageResult<MessageVo> pageResult = new PageResult<>(messagePage);
        pageResult.setList(AppUtils.asVoList(messagePage, MessageVo.class));
        return pageResult;
    }

    /**
     * 获取当前用户未读消息数
     *
     * @return
     */
    public Integer getUserUnreadCount(){
        Message options = new Message();
        options.setToUser(curUser().getId());
        options.setReadFlag(Bool.FALSE);
        return messageMapper.countMessage(options);
    }

    /**
     * 标识单条消息已读
     *
     * @param id
     */
    public void markMessageRead(Integer id){
        Message message = messageMapper.selectByPrimaryKey(id);
        if(message == null){
            return;
        }
        message.setReadFlag(Bool.TRUE);
        message.setUpdateTime(new Date());
        messageMapper.updateByPrimaryKeySelective(message);
    }

    /**
     * 标识所有消息已读
     */
    public void markAllRead(){
        messageMapper.setUserMessageRead(curUser().getId());
    }
}
