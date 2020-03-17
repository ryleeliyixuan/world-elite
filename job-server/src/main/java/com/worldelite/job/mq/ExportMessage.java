package com.worldelite.job.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
@AllArgsConstructor
public class ExportMessage {
    private Byte msgType; //消息类型
    private Long userId; //当前用户
    private String content; //消息内容
}
