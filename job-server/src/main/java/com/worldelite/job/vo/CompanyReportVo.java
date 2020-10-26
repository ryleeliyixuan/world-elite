package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyReport;
import lombok.Data;

import java.util.Date;

/**
 * 企业举报视图对象
 */
@Data
public class CompanyReportVo implements VoConvertable<CompanyReportVo, CompanyReport>{

    private String ownerId; //对象ID
    private Integer optionId; //举报理由选项ID
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间
    private String content; //举报描述

    @Override
    public CompanyReportVo asVo(CompanyReport companyReport) {
        if(companyReport==null) return null;
        BeanUtil.copyProperties(companyReport,this,"id","ownerId");
        setOwnerId(String.valueOf(companyReport.getOwnerId()));
        return this;
    }
}
