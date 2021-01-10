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

    private Integer id; //工作经验ID
    @JSONField(format = "yyyy-MM")
    private Date startTime; //开始时间
    @JSONField(format = "yyyy-MM")
    private Date finishTime; //结束时间
    private String company; //公司名
    private String depart; //部门
    private String post; //职位
    private String description; //描述
    private Integer position; //排序
    private Byte onWork; //是在在职
    private String workType; // 工作类型

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
        setOnWork(resumeExp.getOnWork());
        setWorkType(resumeExp.getWorkType());
        return this;
    }
}
