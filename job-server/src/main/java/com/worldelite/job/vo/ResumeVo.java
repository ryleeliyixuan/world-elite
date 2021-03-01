package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeVo implements VoConvertable<ResumeVo, Resume> {

    private String id; //简历ID
    @ResumeScore(2)
    private String avatar; //头像
    @ResumeScore(3)
    private String name; //名字
    private String title; // 简历名字
    @ResumeScore(2)
    private Byte gender; //性别
    @ResumeScore(2)
    private Integer age; //年龄
    @ResumeScore(2)
    @JSONField(format = "yyyy-MM-dd")
    private Date birth; //生日
    private ResumeEduVo maxResumeEdu; //学历
    @JSONField(format = "yyyy-MM")
    private Date returnTime; //回国时间
    @JSONField(format = "yyyy-MM-dd hh:mm")
    private Date updateTime;
    @JSONField(format = "yyyy-MM")
    private Date graduateTime; //毕业时间
    @ResumeScore(2)
    private String curPlace; //现居地址
    private String phoneCode; //号码区号
    @ResumeScore(2)
    private String phone; //电话号码
    @ResumeScore(3)
    private String email; //邮箱
    @ResumeScore(4)
    private String introduction; //个人介绍
    @ResumeScore(5)
    private String attachResume; //附件简历
    //TODO 这个好像没用
    private String attachOther; // 其他附件
    private String userId; //用户Id
    private String companyId; //企业Id
    private Byte status; //简历状态
    private Byte type; //简历类型

    @ResumeScore(2)
    private Byte maritalStatus; // 政治面貌
    @ResumeScore(1)
    private Byte priority; //简历优先级

    private Integer resumeCompleteProgress; //简历完善进度

    @ResumeScore(15)
    private List<ResumeEduVo> resumeEduList;//教育信息
    @ResumeScore(10)
    private List<ResumeExpVo> resumeExpList; //工作经历
    @ResumeScore(10)
    private List<ResumePracticeVo> resumePracticeList; //实践经验/项目/其它经历
    @ResumeScore(5)
    private List<ResumeSkillVo> resumeSkillList;//能力标签

    private List<ResumeLinkVo> resumeLinkList;//社交主页

    @ResumeScore(5)
    private List<ResumeMergeAttachVo> resumeMergeAttachList;// 其他附件
    @ResumeScore(5)
    private List<ResumeLanguageVo> resumeLanguageList;//语言能力
    @ResumeScore(5)
    private List<ResumeCertificateVo> resumeCertificateList;//证书/奖项
    @ResumeScore(15)
    private UserExpectJobVo userExpectJob; //求职意向

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
        if (resume.getBirth() != null) {
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
