package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyScore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 企业评分视图对象
 */
@Data
public class CompanyScoreVo implements VoConvertable<CompanyScoreVo, CompanyScore>{

    private String id; //评分ID
    private String companyId; //企业ID
    private Integer likes; //点赞数
    private Integer comments; //评论数
    private Integer reports; //举报数
    private Integer hots; //热度
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间
    private String content; //评分内容
    private Integer score; //评分
    private Byte anonymous; //是否匿名

    private UserApplicantVo fromUser; //评分人
    private Boolean like; //是否点赞
    private CompanyReportVo report; //举报信息

    @Override
    public CompanyScoreVo asVo(CompanyScore companyScore) {
        if(companyScore==null) return null;
        BeanUtil.copyProperties(companyScore,this,"id","companyId");
        setId(String.valueOf(companyScore.getId()));
        setCompanyId(String.valueOf(companyScore.getCompanyId()));
        return this;
    }
}
