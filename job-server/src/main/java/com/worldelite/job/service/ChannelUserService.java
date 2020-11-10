package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.ChannelUser;
import com.worldelite.job.form.ChannelUserForm;
import com.worldelite.job.mapper.ChannelUserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ChannelUserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelUserService extends BaseService {

    @Autowired
    private ChannelUserMapper channelUserMapper;

    public ChannelUser save(ChannelUserForm form){
        ChannelUser channelUser = new ChannelUser();
        if(form.getChannelId()==null){
            channelUser = createChannel();
        }else{
            channelUser.setUserId(curUser().getId());
            channelUser.setChannelId(form.getChannelId());
            channelUserMapper.insertSelective(channelUser);
        }
        return channelUser;
    }

    public ChannelUser createChannel(){
        ChannelUser channelUser = new ChannelUser();
        channelUser.setChannelId(AppUtils.nextId());
        channelUser.setUserId(curUser().getId());
        channelUserMapper.insertSelective(channelUser);
        return channelUser;
    }

    public List<ChannelUserVo> listChannelUserVo(){
        ChannelUser channelUser = new ChannelUser();
        channelUser.setUserId(curUser().getId());
        List<ChannelUser> channelUserList = channelUserMapper.selectAndList(channelUser);
        return AppUtils.asVoList(channelUserList,ChannelUserVo.class);
    }

    public void delete(Integer id){
        channelUserMapper.deleteByPrimaryKey(id);
    }
}
