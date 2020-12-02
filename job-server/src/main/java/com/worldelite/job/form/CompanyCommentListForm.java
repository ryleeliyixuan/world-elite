package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    private String title; //帖子标题
    private String[] fromNames; //评论人名
    private String[] toNames; //被评论人名
    private String[] companyNames; //公司名
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime; //开始日期
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime; //结束日期
    private String keyword; //关键词

}
