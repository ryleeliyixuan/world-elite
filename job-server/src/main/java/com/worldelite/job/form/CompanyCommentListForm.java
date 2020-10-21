package com.worldelite.job.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业评论搜索表单
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyCommentListForm extends PageForm{
    private Long id; //评论ID
    private Long fromId; //评论人ID
    private Long toId; //被评论人ID
    private Long ownerId; //对象ID
}
