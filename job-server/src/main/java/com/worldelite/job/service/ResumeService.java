package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.constants.JobApplyStatus;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.mapper.MessageMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class ResumeService extends BaseService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private UserService userService;

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
    private JobService jobService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MessageService messageService;

    /**
     * 获取我的默认简历，如果没有就创建一个空简历
     *
     * @param userId
     * @return
     */
    public ResumeVo getDefaultOrCreate(Long userId) {
        List<Resume> resumeList = resumeMapper.selectByUserId(userId);
        UserVo userVo = userService.getUserInfo(userId);
        Resume defaultResume;
        if (CollectionUtils.isEmpty(resumeList)) {
            Resume resume = new Resume();
            resume.setId(AppUtils.nextId());
            resume.setName(userVo.getName());
            resume.setUserId(userVo.getId());
            resumeMapper.insertSelective(resume);
            defaultResume = resume;
        } else {
            // 第一份为默认显示简历
            defaultResume = resumeList.get(0);
        }
        ResumeVo resumeVo = new ResumeVo().asVo(defaultResume);
        resumeVo.setAvatar(AppUtils.absOssUrl(userVo.getAvatar()));
        resumeVo.setEmail(userVo.getEmail());
        resumeVo.setPhoneCode(userVo.getPhoneCode());
        resumeVo.setPhone(userVo.getPhone());
        resumeVo.setResumeEduList(resumeEduService.getResumeEduList(defaultResume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpList(defaultResume.getId()));
        resumeVo.setResumePracticeList(resumePracticeService.getResumePracticeList(defaultResume.getId()));
        resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(userId));
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillList(defaultResume.getId()));
        resumeVo.setResumeLinkList(resumeLinkService.getResumeLinkList(defaultResume.getId()));
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
        return resumeVo;
    }

    /**
     * 获取简历详情
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeDetail(Long resumeId){
        JobApplyOptions options = new JobApplyOptions();
        options.setCreatorId(curUser().getId());
        options.setResumeId(resumeId);
        final int count = jobApplyMapper.countJobApply(options);
        if(count == 0){
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
        ResumeVo resumeVo = getResumeInfo(resumeId);
        resumeVo.setResumePracticeList(resumePracticeService.getResumePracticeList(resumeId));
        resumeVo.setResumeLinkList(resumeLinkService.getResumeLinkList(resumeId));
        return resumeVo;
    }

    /**
     * 简历 - 保存基础信息
     *
     * @param resumeForm
     * @return
     */
    @Transactional
    public ResumeVo saveBasic(ResumeForm resumeForm) {

        Resume resume = null;
        if (resumeForm.getId() != null) {
            resume = resumeMapper.selectByPrimaryKey(resumeForm.getId());
            checkResumeCreator(resume);
        }

        if (resume == null) {
            resume = new Resume();
            resume.setUserId(curUser().getId());
        }

        resume.setName(resumeForm.getName());
        resume.setBirth(resumeForm.getBirth());
        resume.setGender(resumeForm.getGender());
        resume.setCountryId(resumeForm.getCountryId());
        resume.setCurPlace(resumeForm.getCurPlace());
        resume.setGraduateTime(resumeForm.getGraduateTime());
        resume.setReturnTime(resumeForm.getReturnTime());
        resume.setMaxDegreeId(resumeForm.getMaxDegreeId());
        resume.setMaritalStatus(resumeForm.getMaritalStatus());
        resume.setIntroduction(resumeForm.getIntroduction());

        if (resume.getId() == null) {
            resume.setId(AppUtils.nextId());
            resumeMapper.insertSelective(resume);
        } else {
            resume.setUpdateTime(new Date());
            resumeMapper.updateByPrimaryKeySelective(resume);
        }

        UserForm userForm = new UserForm();
        userForm.setId(curUser().getId());
        userForm.setAvatar(AppUtils.getOssKey(resumeForm.getAvatar()));
        userForm.setName(resumeForm.getName());
        userForm.setGender(resumeForm.getGender());
        userForm.setPhoneCode(resumeForm.getPhoneCode());
        userForm.setPhone(resumeForm.getPhone());

        userService.modifyUser(userForm);

        return new ResumeVo().asVo(resume);
    }

    /**
     * 获取当前用户需要处理的简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult<ApplyResumeVo> getUserApplyResumeList(ApplyResumeListForm listForm) {
        JobApplyOptions options = new JobApplyOptions();
        options.setCreatorId(curUser().getId());
        if (ArrayUtils.isNotEmpty(listForm.getJobIds())) {
            options.setJobIds(StringUtils.join(listForm.getJobIds(), ","));
        }
        if (ArrayUtils.isNotEmpty(listForm.getStatuses())) {
            options.setStatuses(StringUtils.join(listForm.getStatuses(), ","));
        }
        if (ArrayUtils.isNotEmpty(listForm.getDegreeIds())) {
            options.setDegreeIds(StringUtils.join(listForm.getDegreeIds(), ","));
        }
        options.setName(listForm.getName());
        AppUtils.setPage(listForm);
        Page<JobApply> jobApplyPage = (Page<JobApply>) jobApplyMapper.selectApplyResumeList(options);
        PageResult<ApplyResumeVo> pageResult = new PageResult<>(jobApplyPage);
        List<ApplyResumeVo> applyResumeVoList = new ArrayList<>(jobApplyPage.size());
        for (JobApply jobApply : jobApplyPage) {
            ApplyResumeVo applyResumeVo = new ApplyResumeVo();
            applyResumeVo.setId(jobApply.getId());
            applyResumeVo.setApplyStatus(jobApply.getStatus());
            applyResumeVo.setJob(jobService.getJobInfo(jobApply.getJobId(), false));
            applyResumeVo.setResume(getResumeInfo(jobApply.getResumeId()));
            applyResumeVo.setTime(jobApply.getCreateTime());
            applyResumeVoList.add(applyResumeVo);
        }
        pageResult.setList(applyResumeVoList);
        return pageResult;
    }

    /**
     * 处理简历
     */
    public void handleApplyResume(JobApplyForm applyResumeForm) {
        JobApply jobApply = jobApplyMapper.selectByPrimaryKey(applyResumeForm.getId());
        if (jobApply == null) {
            throw new ServiceException(String.valueOf(applyResumeForm.getId()), ApiCode.OBJECT_NOT_FOUND);
        }

        Resume resume = resumeMapper.selectByPrimaryKey(jobApply.getResumeId());
        if (resume == null) {
            throw new ServiceException(String.valueOf(applyResumeForm.getId()), ApiCode.OBJECT_NOT_FOUND);
        }

        JobVo job = jobService.getJobInfo(jobApply.getJobId(), true);
        if (!job.getCreatorId().equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }

        jobApply.setStatus(applyResumeForm.getStatus());
        jobApplyMapper.updateByPrimaryKeySelective(jobApply);

        if(applyResumeForm.getStatus() == JobApplyStatus.VIEW.value){
            return;
        }

        // 发送站内和邮件消息
        UserVo toUser = userService.getUserInfo(resume.getUserId());
        final String jobPlaceholder = String.format("%s.%s", job.getCompanyUser().getCompany().getName(), job.getName());
        EmailForm emailForm = null;
        String messageContent = null;
        if (applyResumeForm.getStatus() == JobApplyStatus.CANDIDATE.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_CANDIDATE);
            messageContent = message("message.apply.candidate", jobPlaceholder);
        } else if (applyResumeForm.getStatus() == JobApplyStatus.INTERVIEW.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_INTERVIEW);
            messageContent = message("message.apply.interview", jobPlaceholder);
        } else if (applyResumeForm.getStatus() == JobApplyStatus.OFFER.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_OFFER);
            messageContent = message("message.apply.offer", jobPlaceholder);
        } else if (applyResumeForm.getStatus() == JobApplyStatus.ABANDON.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_ABANDON);
            messageContent = message("message.apply.abandon", jobPlaceholder);
        }

        if (emailForm != null) {
            emailForm.setEmailBody(emailForm.getEmailBody().replace("${JOB}", jobPlaceholder));
            emailForm.setAddress(toUser.getEmail());
            emailService.sendEmail(emailForm);
        }

        if(messageContent != null){
            Message message = new Message();
            message.setFromUser(curUser().getId());
            message.setToUser(resume.getUserId());
            message.setContent(messageContent);
            messageService.sendMessage(message);
        }
    }

    /**
     * 检查简历创建者是否登录用户
     *
     * @param resumeId
     */
    public void checkResumeCreator(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        checkResumeCreator(resume);
    }

    /**
     * 获取简历信息
     *
     * @param resumeId
     * @return
     */
    private ResumeVo getResumeInfo(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        return toResumeVo(resume);
    }

    private ResumeVo toResumeVo(Resume resume) {
        ResumeVo resumeVo = new ResumeVo().asVo(resume);
        UserVo userVo = userService.getUserInfo(resume.getUserId());
        resumeVo.setAvatar(AppUtils.absOssUrl(userVo.getAvatar()));
        resumeVo.setEmail(userVo.getEmail());
        resumeVo.setPhoneCode(userVo.getPhoneCode());
        resumeVo.setPhone(userVo.getPhone());
        List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
        resumeVo.setResumeEduList(resumeEduVoList);
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillList(resume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpList(resume.getId()));
        if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
            resumeVo.setMaxResumeEdu(JSON.parseObject(JSON.toJSONString(resumeEduVoList.get(0)), ResumeEduVo.class));
        }
        return resumeVo;
    }

    private void checkResumeCreator(Resume resume) {
        if (resume != null && resume.getUserId() != null && !resume.getUserId().equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
    }
}
