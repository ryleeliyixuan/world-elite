package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyHistory;
import lombok.Data;

import java.util.Date;

/**
 * 企业发展路径视图对象
 */
@Data
public class CompanyHistoryVo implements VoConvertable<CompanyHistoryVo, CompanyHistory>{

    private Integer id; //发展路径ID
    private String companyId; //公司ID
    private String eventTime; //标志时间
    private String event; //标志事件描述

    @Override
    public CompanyHistoryVo asVo(CompanyHistory companyHistory) {
        if(companyHistory==null) return null;
        setId(companyHistory.getId());
        setCompanyId(String.valueOf(companyHistory.getCompanyId()));
        setEventTime(companyHistory.getEventTime());
        setEvent(companyHistory.getEvent());
        return this;
    }
}
