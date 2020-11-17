package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyRecruit;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyRecruitVo implements VoConvertable<CompanyRecruitVo, CompanyRecruit>{

    private Integer id; //ID
    private String companyId; //公司ID
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date time; //时间
    private String event; //进度事件

    @Override
    public CompanyRecruitVo asVo(CompanyRecruit companyRecruit) {
        if(companyRecruit==null) return null;
        setId(companyRecruit.getId());
        setCompanyId(String.valueOf(companyRecruit.getCompanyId()));
        setTime(companyRecruit.getTime());
        setEvent(companyRecruit.getEvent());
        return this;
    }
}
