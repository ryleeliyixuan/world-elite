package com.worldelite.job.vo;

import com.worldelite.job.entity.Message;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class MessageVo implements VoConvertable<MessageVo, Message>{

    private Integer id;
    private UserVo fromUser;
    private String content;
    private String url;
    private Byte readFlag;

    @Override
    public MessageVo asVo(Message message) {
        setId(message.getId());
        setContent(message.getContent());
        setUrl(message.getUrl());
        setReadFlag(message.getReadFlag());
        return this;
    }
}
