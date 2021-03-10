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
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ApplyResumeVo;
import com.worldelite.job.vo.JobVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JobApplyService extends BaseService {

    @Autowired
    private JobApplyMapper jobApplyMapper;

    @Autowired
    private JobMapper jobMapper;

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
    private ConfigService configService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserApplicantService userApplicantService;

    /**
     * 申请职位
     *
     * @param resumeId 简历ID
     * @param jobId    职位ID
     */
    public JobApply applyJob(Long resumeId, Long jobId, Byte status) {
        //如果简历ID不存在，则使用当前登录用户默认简历
        if (resumeId == null) {
            ResumeService resumeService = ResumeServiceFactory.getDefaultService();
            ResumeDetail resumeDetail = resumeService.getDefaultOrCreate();
            //默认简历也不存在，则判定为数据异常
            if (resumeDetail == null) {
                throw new ServiceException(message("job.apply.no.resume"), ApiCode.UNCOMPLETE_RESUME);
            }
            resumeId = resumeDetail.getResumeId();
        }

        Job job = jobMapper.selectSimpleById(jobId);
        if (job == null) {
            throw new ServiceException(ApiCode.INVALID_OPERATION);
        }

        JobApply newJobApply = new JobApply();
        newJobApply.setJobId(jobId);
        newJobApply.setUserId(curUser().getId());
        if (status == null) {
            status = JobApplyStatus.APPLY.value;
        }
        newJobApply.setStatus(status);
        newJobApply.setType(JobApplyType.APPLICANT.value);
        newJobApply.setResumeId(resumeId);
        jobApplyMapper.insertSelective(newJobApply);

        // 给职位创建者发消息
        Message message = new Message();
        message.setFromUser(curUser().getId());
        message.setToUser(job.getCreatorId());
        message.setContent(message("message.job.apply", job.getName()));
        message.setUrl(String.format("/manage-resume?jobIds=%s", job.getId()));
        message.setMsgType((byte) 1);
        messageService.sendMessage(message);
        return newJobApply;
    }

    /**
     * 获取需要处理的简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult<ApplyResumeVo> getApplyResumeList(ApplyResumeListForm listForm) {
        JobApplyOptions options = new JobApplyOptions();
        options.setCreatorId(listForm.getCreatorId());
        if (listForm.getCompanyId() == null) {
            CompanyUser companyUser = companyUserMapper.selectByUserId(listForm.getCreatorId());
            options.setCompanyId(companyUser.getCompanyId());
        } else
            options.setCompanyId(listForm.getCompanyId());

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
            applyResumeVo.setCommentVos(corporateCommentService.getCommentsByResumeId(jobApply.getId()));
            //获取简历基础信息
            ResumeService resumeService = ResumeServiceFactory.getDefaultService();
            Resume resume = resumeService.getResumeBasic(jobApply.getResumeId());
            //一份职位申请，如果对应简历不存在，判定为数据异常
            if (resume == null) {
                log.warn("用户[{}]的简历[{}]不存在", jobApply.getUserId(), jobApply.getResumeId());
                continue;
            }
            //根据简历类型获取简历详情
            resumeService = ResumeServiceFactory.getResumeService(resume.getType());
            ResumeDetail resumeDetail = resumeService.getResumeDetail(resume.getId());
            applyResumeVo.setResume(resumeService.toResumeVo(resumeDetail));
            applyResumeVo.setJob(jobService.getJobInfo(jobApply.getJobId(), false));
            applyResumeVo.setTagVos(corporateTagService.getTagsByResumeId(resume.getId()));
            applyResumeVo.setTime(jobApply.getCreateTime());
            applyResumeVoList.add(applyResumeVo);
        }
        pageResult.setList(applyResumeVoList);
        return pageResult;
    }

    /**
     * 处理简历
     *
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

        JobVo job = jobService.getJobInfo(jobApply.getJobId(), true);
        Long creatorId = Long.valueOf(job.getCreatorId());
        if (!job.getId().equals("0") && !creatorId.equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
        //只有未查看状态才能变换到查看状态
        if (applyResumeForm.getStatus() == JobApplyStatus.VIEW.value && jobApply.getStatus() != JobApplyStatus.APPLY.value) {
            return;
        }
        jobApply.setStatus(applyResumeForm.getStatus());
        jobApplyMapper.updateByPrimaryKeySelective(jobApply);

        if (applyResumeForm.getStatus() == JobApplyStatus.VIEW.value) {
            return;
        }

        // 发送站内和邮件消息
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resume.getType());
        ResumeDetail resumeDetail = resumeService.getResumeDetail(resume.getId());
        String jobPlaceholder = "";
        if (job.getId().equals("0")) {
            return;
        } else {
            jobPlaceholder = String.format("%s.%s", job.getCompanyUser().getCompany().getName(), job.getName());
        }
        EmailForm emailForm = null;
        String messageContent = null;
        Byte msgType = null;
        if (applyResumeForm.getStatus() == JobApplyStatus.CANDIDATE.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_CANDIDATE);
            messageContent = message("message.apply.candidate", jobPlaceholder);
            msgType = (byte) 1;
        } else if (applyResumeForm.getStatus() == JobApplyStatus.INTERVIEW.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_INTERVIEW);
            messageContent = message("message.apply.interview", jobPlaceholder);
            msgType = (byte) 1;
        } else if (applyResumeForm.getStatus() == JobApplyStatus.OFFER.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_OFFER);
            messageContent = message("message.apply.offer", jobPlaceholder);
            msgType = (byte) 1;
        } else if (applyResumeForm.getStatus() == JobApplyStatus.ABANDON.value) {
            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_ABANDON);
            messageContent = message("message.apply.abandon", jobPlaceholder);
            msgType = (byte) 0;
        }

        if (emailForm != null) {
            final String emailBody = emailForm.getEmailBody().replace("${JOB}", jobPlaceholder).replace("${DETAIL_URL}", AppUtils.wholeWebUrl("/apply-jobs"));
            emailForm.setEmailBody(emailBody);
            emailForm.setAddress(resumeDetail.getEmail());
            emailService.sendEmail(emailForm);
        }

        if (messageContent != null) {
            Message message = new Message();
            message.setFromUser(curUser().getId());
            message.setToUser(resume.getUserId());
            message.setContent(messageContent);
            message.setMsgType(msgType);
            messageService.sendMessage(message);
        }
    }

}
