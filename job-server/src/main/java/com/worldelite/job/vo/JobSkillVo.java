package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.JobSkill;
import lombok.Data;

import java.util.Date;

@Data
public class JobSkillVo implements VoConvertable<JobSkillVo, JobSkill> {
    private Long id; //职位技能ID
    private String name; //职位技能名
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间

    @Override
    public JobSkillVo asVo(JobSkill jobSkill) {
        setId(jobSkill.getId());
        setName(jobSkill.getName());
        setCreateTime(jobSkill.getCreateTime());
        return this;
    }
}
