package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyComment;
import lombok.Data;

import java.util.Date;

/**
 * 企业评论视图对象
 */
@Data
public class PostCommentVo implements VoConvertable<PostCommentVo, CompanyComment>{

    private String id; //评论ID
    private String ownerId; //对象ID
    private UserApplicantVo fromUser; //评论人用户信息
    private UserApplicantVo toUser; //被评论人用户信息
    private Byte type; //评论类型
    private Integer likes; //点赞数
    private Integer reports; //举报数
    private Integer comments; //评论数
    private Integer hots; //热度
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间
    private String content; //评论内容

    private Boolean like; //是否点过赞
    private CompanyReportVo report; //举报信息

    private String title; //帖子标题
    private String companyName; //企业名

    @Override
    public PostCommentVo asVo(CompanyComment companyComment) {
        if(companyComment==null) return null;
        BeanUtil.copyProperties(companyComment,this,"id","ownerId");
        setId(String.valueOf(companyComment.getId()));
        setOwnerId(String.valueOf(companyComment.getOwnerId()));
        return this;
    }
}
