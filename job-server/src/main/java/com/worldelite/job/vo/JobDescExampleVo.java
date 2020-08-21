package com.worldelite.job.vo;

import com.worldelite.job.entity.JobDescExample;
import com.worldelite.job.entity.JobIndustry;
import lombok.Data;

import java.util.Date;

@Data
public class JobDescExampleVo implements VoConvertable<JobDescExampleVo, JobDescExample> {
    private long id; //主键Id
    private long categoryId; //职位类型Id
    private String description; //职位描述
    private Date createTime; //创建时间

    @Override
    public JobDescExampleVo asVo(JobDescExample jobDescExample) {
        setId(jobDescExample.getId());
        setCategoryId(jobDescExample.getCategoryId());
        setDescription(jobDescExample.getDescription());
        setCreateTime(jobDescExample.getCreateTime());
        return this;
    }
}
