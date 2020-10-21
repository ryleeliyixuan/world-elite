package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 企业帖子视图对象
 */
@Data
public class CompanyPostVo implements VoConvertable<CompanyPostVo, CompanyPost>{

    private String id; //帖子ID
    private String fromId; //发帖人
    private String companyId; //企业ID
    private Integer cliqueId; //圈子ID
    private String title; //帖子标题
    private String[] tags; //标签列表
    private Integer likes; //点赞数
    private Integer comments; //评论数
    private Integer reports; //举报数
    private Integer hots; //热度
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间
    private String content; //帖子内容

    @Override
    public CompanyPostVo asVo(CompanyPost companyPost) {
        setId(String.valueOf(companyPost.getId()));
        setFromId(String.valueOf(companyPost.getFromId()));
        setCompanyId(String.valueOf(companyPost.getCompanyId()));
        setCliqueId(companyPost.getCliqueId());
        setTitle(companyPost.getTitle());
        setContent(companyPost.getContent());
        setCreateTime(companyPost.getCreateTime());
        setUpdateTime(companyPost.getUpdateTime());
        setLikes(companyPost.getLikes());
        setComments(companyPost.getComments());
        setReports(companyPost.getReports());
        setHots(companyPost.getHots());
        if(StringUtils.isNotEmpty(companyPost.getTags())){
            setTags(companyPost.getTags().split(","));
        }else{
            setTags(new String[]{});
        }
        return this;
    }
}
