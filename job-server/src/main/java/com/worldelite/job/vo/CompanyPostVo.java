package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.CompanyComment;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyReport;
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
    private String image; //帖子图片
    private Byte recommend; //是否设为精品/优质

    private Byte forbidden; //是否禁言
    private CompanyVo company; //企业信息

    private UserApplicantVo fromUser; //发帖人
    private Boolean like; //是否点赞
    private CompanyReportVo report; //举报信息

    @Override
    public CompanyPostVo asVo(CompanyPost companyPost) {
        if(companyPost==null) return null;
        BeanUtil.copyProperties(companyPost,this,"id","companyId","tags");
        setId(String.valueOf(companyPost.getId()));
        setCompanyId(String.valueOf(companyPost.getCompanyId()));
        if(StringUtils.isNotEmpty(companyPost.getTags())){
            setTags(companyPost.getTags().split(","));
        }else{
            setTags(new String[]{});
        }
        return this;
    }
}
