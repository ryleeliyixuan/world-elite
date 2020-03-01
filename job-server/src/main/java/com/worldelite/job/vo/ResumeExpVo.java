package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeExperience;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeExpVo extends EditFlag implements VoConvertable<ResumeExpVo, ResumeExperience>{

    private Integer id;
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private String company;
    private String depart;
    private String post;
    private String description;
    private Integer position;

    @Override
    public ResumeExpVo asVo(ResumeExperience resumeExp) {
        setId(resumeExp.getId());
        setStartTime(resumeExp.getStartTime());
        setFinishTime(resumeExp.getFinishTime());
        setCompany(resumeExp.getCompany());
        setDepart(resumeExp.getDepart());
        setPost(resumeExp.getPost());
        setDescription(resumeExp.getDescription());
        setPosition(resumeExp.getPosition());
        return this;
    }
}
