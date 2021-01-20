package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.*;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeVo implements VoConvertable<ResumeVo, Resume>{

    private String id; //简历ID
    @ResumeScore
    private String avatar; //头像
    @ResumeScore
    private String name; //名字
    private String title; // 简历名字
    @ResumeScore
    private Byte gender; //性别
    @ResumeScore
    private Integer age; //年龄
    @ResumeScore
    @JSONField(format = "yyyy-MM-dd")
    private Date birth; //生日
    private ResumeEduVo maxResumeEdu; //学历
    @ResumeScore
    @JSONField(format = "yyyy-MM")
    private Date returnTime; //回国时间
    @ResumeScore
    @JSONField(format = "yyyy-MM")
    private Date updateTime;
    @ResumeScore
    @JSONField(format = "yyyy-MM")
    private Date graduateTime; //毕业时间
    @ResumeScore
    private String curPlace; //现居地址
    private String phoneCode; //号码区号
    @ResumeScore
    private String phone; //电话号码
    private String email; //邮箱
    @ResumeScore(4)
    private String introduction; //个人介绍
    private String attachResume; //附件简历
    private String attachOther; // 其他附件
    private String userId; //用户Id
    private String companyId; //企业Id
    private Byte status; //简历状态
    private Byte type; //简历类型

    private Byte maritalStatus; // 政治面貌
    private Byte priority;

    private Integer resumeCompleteProgress; //简历完善进度

    @ResumeScore(4)
    private List<ResumeEduVo> resumeEduList;
    @ResumeScore(4)
    private List<ResumeExpVo> resumeExpList;
    @ResumeScore(4)
    private List<ResumePracticeVo> resumePracticeList;
    @ResumeScore
    private List<ResumeSkillVo> resumeSkillList;

    private List<ResumeLinkVo> resumeLinkList;

    public void setResumeMergeAttachList(List<ResumeMergeAttachVo> resumeMergeAttachList) {
        this.resumeMergeAttachList = resumeMergeAttachList;
    }

    private List<ResumeMergeAttachVo>resumeMergeAttachList;

    public void setResumeLanguageList(List<ResumeLanguageVo> resumeLanguageList) {
        this.resumeLanguageList = resumeLanguageList;
    }

    private List<ResumeLanguageVo>resumeLanguageList;

    public void setResumeCertificateList(List<ResumeCertificateVo> resumeCertificateList) {
        this.resumeCertificateList = resumeCertificateList;
    }

    private List<ResumeCertificateVo>resumeCertificateList;

    public UserExpectJobVo getUserExpectJob() {
        return userExpectJob;
    }

    public void setUserExpectJob(UserExpectJobVo userExpectJob) {
        this.userExpectJob = userExpectJob;
    }

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
        setUserId(String.valueOf(resume.getUserId()));
        setName(resume.getName());
        setGender(resume.getGender());
        setBirth(resume.getBirth());
        setStatus(resume.getStatus());
        setType(resume.getType());
        setAttachResume(AppUtils.absOssUrl(resume.getAttachResume()));
        if(resume.getBirth() != null){
            setAge(TimeUtils.getCurrentAge(resume.getBirth()));
        }
        setReturnTime(resume.getReturnTime());
        setGraduateTime(resume.getGraduateTime());
        setCurPlace(resume.getCurPlace());
        setIntroduction(resume.getIntroduction());
        setAvatar(resume.getAvatar());
        setPriority(resume.getPriority());
        setEmail(resume.getEmail());
        return this;
    }
}
