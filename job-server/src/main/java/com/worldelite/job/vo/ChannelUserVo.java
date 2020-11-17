package com.worldelite.job.vo;

import com.worldelite.job.entity.ChannelUser;
import lombok.Data;

@Data
public class ChannelUserVo implements VoConvertable<ChannelUserVo, ChannelUser>{
    private Integer id; //ID
    private String userId; //用户ID
    private String channelId; //房间号

    @Override
    public ChannelUserVo asVo(ChannelUser channelUser) {
        if(channelUser==null) return null;
        setId(channelUser.getId());
        setUserId(String.valueOf(channelUser.getUserId()));
        setChannelId(String.valueOf(channelUser.getChannelId()));
        return this;
    }
}
