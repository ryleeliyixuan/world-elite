package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.JobApplyStatus;
import com.worldelite.job.constants.JobApplyType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.*;
import com.worldelite.job.service.sdk.ResumeSDK;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业简历库服务类
 */
@Slf4j
@Service
public class ResumeRepositoryService extends BaseService{

    @Autowired
    private ResumeApplicantMapper resumeApplicantMapper;

    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private UserRepositoryMapper userRepositoryMapper;

    @Autowired
    private ResumeRepositoryMapper resumeRepositoryMapper;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserApplicantService userApplicantService;

    @Autowired
    private ResumeEduService resumeEduService;

    @Autowired
    private ResumeExpService resumeExpService;

    @Autowired
    private ResumePracticeService resumePracticeService;

    @Autowired
    private UserExpectJobService userExpectJobService;

    @Autowired
    private ResumeSkillService resumeSkillService;

    @Autowired
    private ResumeLinkService resumeLinkService;

    @Autowired
    private DictService dictService;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private ResumeSDK resumeSDK;

    /**
     * 简历投递情况统计
     * @param resumeId
     * @param userId
     * @param resumeVo
     * @return
     */
    private int getApplyOfferCount(Long resumeId,Long userId,ResumeVo resumeVo){
        JobApplyOptions applyOptions = new JobApplyOptions();
        applyOptions.setResumeId(resumeId);
        applyOptions.setUserId(userId);
        final int applyTotalCount = jobApplyMapper.countJobApply(applyOptions);
        resumeVo.setApplyTotalCount(applyTotalCount);
        applyOptions.setStatuses(String.valueOf(JobApplyStatus.APPLY.value));
        final int applyingCount = jobApplyMapper.countJobApply(applyOptions);
        resumeVo.setApplyingCount(applyingCount);
        applyOptions.setStatuses(String.valueOf(JobApplyStatus.CANDIDATE.value));
        final int applyCandidateCount = jobApplyMapper.countJobApply(applyOptions);
        resumeVo.setApplyCandidateCount(applyCandidateCount);
        applyOptions.setStatuses(String.valueOf(JobApplyStatus.INTERVIEW.value));
        final int applyInterviewCount = jobApplyMapper.countJobApply(applyOptions);
        resumeVo.setApplyInterviewCount(applyInterviewCount);
        applyOptions.setStatuses(String.valueOf(JobApplyStatus.OFFER.value));
        return  jobApplyMapper.countJobApply(applyOptions);
    }

    @Transactional
    public void saveAttachment(String attachmentName){
        //获取OSS路径
        String fileName = AppUtils.getOssKey(attachmentName);
        String filePath = AppUtils.absOssUrl(fileName);
        JSONObject result = resumeSDK.parse(filePath);
        //保存基本信息
        ResumeForm resumeForm = resumeSDK.getResume(result);
        resumeForm.setAttachResume(fileName);
        resumeService.saveBasic(resumeForm);
        //保存教育信息
        List<ResumeEduForm> resumeEduFormList = resumeSDK.getResumeEdu(result);
        for(ResumeEduForm eduForm:resumeEduFormList){
            resumeEduService.saveResumeEdu(eduForm);
        }
        //保存工作经验
        List<ResumeExpForm> resumeExpFormList = resumeSDK.getResumeExperience(result);
        for(ResumeExpForm expForm:resumeExpFormList){
            resumeExpService.saveResumeExp(expForm);
        }
        //保存实践经验
        List<ResumePracticeForm> practiceFormList = resumeSDK.getResumePractice(result);
        for(ResumePracticeForm practiceForm:practiceFormList){
            resumePracticeService.saveResumePractice(practiceForm);
        }
        //能力标签
        ResumeSkillForm resumeSkillForm = resumeSDK.getResumeSkill(result);
        resumeSkillService.saveResumeSkill(resumeSkillForm);
        //社交主页
        ResumeLinkForm resumeLinkForm = resumeSDK.getResumeLink(result);
        resumeLinkService.saveResumeLink(resumeLinkForm);
    }
}
