package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.Bool;
import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.entity.Message;
import com.worldelite.job.form.MessageListForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.FavoriteMapper;
import com.worldelite.job.mapper.MessageMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.MessageVo;
import com.worldelite.job.vo.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class MessageService extends BaseService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private FavoriteMapper favoriteMapper;

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

    public void deleteMessage(Integer id){
        Message message = messageMapper.selectByPrimaryKey(id);
        if(message.getToUser().equals(curUser().getId())){
            messageMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除所有消息
     */
    public void deleteAllMessage(){
        messageMapper.deleteUserMessages(curUser().getId());
    }

    /**
     * 给订阅了企业的用户发送工作消息
     */
    public void sendJobSubscribeMessage(JobVo jobVo){
        int page = 1;
        int pageSize = 20;
        List<Favorite> favoriteList;
        CompanyVo companyVo = jobVo.getCompanyUser().getCompany();
        Favorite options = new Favorite();
        options.setType(FavoriteType.COMPANY.value);
        options.setObjectId(NumberUtils.toLong(companyVo.getId()));
        do{
            PageHelper.startPage(page, pageSize, false);
            favoriteList = favoriteMapper.selectAndList(options);
            for(Favorite favorite: favoriteList){
                Message message = new Message();
                message.setFromUser(Long.valueOf(jobVo.getCreatorId()));
                message.setToUser(favorite.getUserId());
                message.setContent(message("message.job.subscribe", companyVo.getName(), jobVo.getName()));
                message.setUrl(String.format("/job/%s", jobVo.getId()));
                messageMapper.insertSelective(message);
            }
            page++;
        }while(CollectionUtils.isNotEmpty(favoriteList));
    }
}
