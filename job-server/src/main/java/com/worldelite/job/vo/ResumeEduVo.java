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

    private Integer id; //学历ID
    private String schoolName; //学校名
    private String majorName; //专业
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    private DictVo degree; //学历
    private Double gpa; //gpa

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
