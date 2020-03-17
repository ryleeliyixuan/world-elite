package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.entity.ResumeLink;
import com.worldelite.job.entity.ResumePractice;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeVo implements VoConvertable<ResumeVo, Resume>{

    private String id;
    @ResumeScore
    private String avatar;
    @ResumeScore
    private String name;
    @ResumeScore
    private Byte gender;
    @ResumeScore
    private Integer age;
    @ResumeScore
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;
    private ResumeEduVo maxResumeEdu;
    private Byte maritalStatus;
    @ResumeScore
    @JSONField(format = "yyyy-MM")
    private Date returnTime;
    @ResumeScore
    @JSONField(format = "yyyy-MM")
    private Date graduateTime;
    @ResumeScore
    private String curPlace;
    private String phoneCode;
    @ResumeScore
    private String phone;
    private String email;
    @ResumeScore(2)
    private String introduction;

    private Integer resumeCompleteProgress;

    @ResumeScore(2)
    private List<ResumeEduVo> resumeEduList;
    @ResumeScore(2)
    private List<ResumeExpVo> resumeExpList;
    @ResumeScore(2)
    private List<ResumePracticeVo> resumePracticeList;
    @ResumeScore
    private List<ResumeSkillVo> resumeSkillList;

    private List<ResumeLinkVo> resumeLinkList;

    private UserExpectJobVo userExpectJob;

    //已投递职位总数
    private Integer applyTotalCount;

    // 投递中的岗位数量
    private Integer applyingCount;

    // 通过初筛数量
    private Integer applyCandidateCount;

    // 进入面试数量
    private Integer applyInterviewCount;

    // 已录用
    private Integer applyOfferCount;

    @Override
    public ResumeVo asVo(Resume resume) {
        setId(String.valueOf(resume.getId()));
        setName(resume.getName());
        setGender(resume.getGender());
        setBirth(resume.getBirth());
        if(resume.getBirth() != null){
            setAge(TimeUtils.getCurrentAge(resume.getBirth()));
        }
        setReturnTime(resume.getReturnTime());
        setGraduateTime(resume.getGraduateTime());
        setCurPlace(resume.getCurPlace());
        setIntroduction(resume.getIntroduction());
        return this;
    }
}
