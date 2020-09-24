package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.constants.JobApplyStatus;
import com.worldelite.job.constants.JobApplyType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ApplyResumeListForm;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.form.JobApplyForm;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JobApplyService extends BaseService{


    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserCorporateCommentService corporateCommentService;

    @Autowired
    private UserCorporateTagService corporateTagService;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private ResumeApplicantService resumeApplicantService;

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserApplicantService userApplicantService;

    /**
     * 获取需要处理的简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult<ApplyResumeVo> getApplyResumeList(ApplyResumeListForm listForm) {
        JobApplyOptions options = new JobApplyOptions();
        options.setCreatorId(listForm.getCreatorId());
        CompanyUser companyUser = companyUserMapper.selectByUserId(listForm.getCreatorId());
        options.setCompanyId(companyUser.getCompanyId());
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
            applyResumeVo.setType(jobApply.getType());
            applyResumeVo.setCommentVos(corporateCommentService.getCommentsByJobApplyId(jobApply.getId()));
            if(applyResumeVo.getType() == JobApplyType.APPLICANT.value){
                applyResumeVo.setJob(jobService.getJobInfo(jobApply.getJobId(), false));
                applyResumeVo.setResume(resumeApplicantService.getResumeDetail(jobApply.getResumeId()));
            }
            if(applyResumeVo.getType() == JobApplyType.REPOSITORY.value){
                JobVo jobVo = new JobVo();
                jobVo.setName("");
                applyResumeVo.setResume(resumeRepositoryService.getResumeDetail(jobApply.getResumeId()));
                applyResumeVo.setJob(jobVo);
            }
            applyResumeVo.setTagVos(corporateTagService.getTagsByJobApplyId(jobApply.getId()));
            applyResumeVo.setTime(jobApply.getCreateTime());
            applyResumeVoList.add(applyResumeVo);
        }
        pageResult.setList(applyResumeVoList);
        return pageResult;
    }

    /**
     * 处理简历
     * @param applyResumeForm
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

        if(jobApply.getType() == JobApplyType.APPLICANT.value) {
            JobVo job = jobService.getJobInfo(jobApply.getJobId(), true);
            if (!job.getCreatorId().equals(curUser().getId())) {
                throw new ServiceException(ApiCode.PERMISSION_DENIED);
            }
            jobApply.setStatus(applyResumeForm.getStatus());
            jobApplyMapper.updateByPrimaryKeySelective(jobApply);

            if(applyResumeForm.getStatus() == JobApplyStatus.VIEW.value){
                return;
            }

            ResumeApplicant resumeApplicant = resumeApplicantService.selectByResumeId(resume.getId());

            // 发送站内和邮件消息
            UserApplicantVo toUser = userApplicantService.getUserInfo(resumeApplicant.getUserId());
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
                final String emailBody = emailForm.getEmailBody().replace("${JOB}", jobPlaceholder).replace("${DETAIL_URL}", AppUtils.wholeWebUrl("/apply-jobs" ));
                emailForm.setEmailBody(emailBody);
                emailForm.setAddress(toUser.getEmail());
                emailService.sendEmail(emailForm);
            }

            if(messageContent != null){
                Message message = new Message();
                message.setFromUser(curUser().getId());
                message.setToUser(resumeApplicant.getUserId());
                message.setContent(messageContent);
                messageService.sendMessage(message);
            }
        }else{
            jobApply.setStatus(applyResumeForm.getStatus());
            jobApplyMapper.updateByPrimaryKeySelective(jobApply);

            if(applyResumeForm.getStatus() == JobApplyStatus.VIEW.value){
                return;
            }
        }
    }

}
