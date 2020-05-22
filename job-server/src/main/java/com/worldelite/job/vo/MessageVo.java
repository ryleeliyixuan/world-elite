package com.worldelite.job.vo;

import com.worldelite.job.entity.Message;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class MessageVo implements VoConvertable<MessageVo, Message>{

    private Integer id; // 消息ID
    private UserVo fromUser; //来自用户
    private String content; //消息内容
    private String url; // 查看url
    private Byte readFlag; //已读标志

    @Override
    public MessageVo asVo(Message message) {
        setId(message.getId());
        setContent(message.getContent());
        setUrl(message.getUrl());
        setReadFlag(message.getReadFlag());
        return this;
    }
}
