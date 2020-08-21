package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.JobIndustry;
import lombok.Data;

import java.util.Date;

@Data
public class JobIndustryVo implements VoConvertable<JobIndustryVo,JobIndustry> {
    private Long id; //职位行业领域ID
    private String name; //职位行业领域
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间

    @Override
    public JobIndustryVo asVo(JobIndustry jobIndustry) {
        setId(jobIndustry.getId());
        setName(jobIndustry.getName());
        setCreateTime(jobIndustry.getCreateTime());
        return this;
    }
}
