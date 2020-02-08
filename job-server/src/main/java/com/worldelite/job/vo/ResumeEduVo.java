package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeEdu;
import com.worldelite.job.service.DictService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeEduVo extends EditFlag implements VoConvertable<ResumeEduVo, ResumeEdu>{

    private Integer id;
    private String schoolName;
    private String majorName;
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private DictVo degree;
    private Double gpa;

    @Override
    public ResumeEduVo asVo(ResumeEdu resumeEdu) {
        setId(resumeEdu.getId());
        setSchoolName(resumeEdu.getSchoolName());
        setMajorName(resumeEdu.getMajorName());
        setStartTime(resumeEdu.getStartTime());
        setFinishTime(resumeEdu.getFinishTime());
        setGpa(resumeEdu.getGpa());
        DictVo degree = DictService.getInstance().getById(resumeEdu.getDegreeId());
        setDegree(degree);
        return this;
    }
}
