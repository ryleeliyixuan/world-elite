package com.worldelite.job.vo;

import com.worldelite.job.entity.ResumeSkill;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeSkillVo implements VoConvertable<ResumeSkillVo, ResumeSkill>{

    private Integer id;
    private String name;
    private Byte level;

    @Override
    public ResumeSkillVo asVo(ResumeSkill resumeSkill) {
        setId(resumeSkill.getId());
        setName(resumeSkill.getName());
        setLevel(resumeSkill.getLevel());
        return this;
    }
}
