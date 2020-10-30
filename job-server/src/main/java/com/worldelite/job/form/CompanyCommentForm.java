package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业评论表单
 */
@Data
public class CompanyCommentForm {
    private Long id; //评论ID
    private Long ownerId; //对象ID
    private String content; //评分内容
    private Byte type; //评论类型
}
