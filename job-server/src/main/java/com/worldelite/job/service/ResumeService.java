package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.ResumeAttachMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.read.ResumeFileRead;
import com.worldelite.job.service.read.ResumeFileReadFactory;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.FormUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URL;
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
    private UserApplicantService userService;

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
    private DictService dictService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ResumeAttachService resumeAttachService;

    @Autowired
    private ResumeFileReadFactory resumeFileReadFactory;

    @Autowired
    private ResumeAttachMapper resumeAttachMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;

    /**
     * 获取我的默认简历，如果没有就创建一个空简历
     *
     * @param userId
     * @return
     */
    public ResumeVo getDefaultOrCreate(Long userId) {
        List<Resume> resumeList = resumeMapper.selectByUserId(userId);
        UserApplicantVo userVo = userService.getUserInfo(userId);
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
     * 获取简历列表
     *
     * @param listForm
     * @return
     */
    public PageResult getResumeList(ResumeListForm listForm){

        if(listForm.getSalaryRangeId() != null){
            DictVo salaryRange =  dictService.getById(listForm.getSalaryRangeId());
            if(salaryRange != null){
                String[] values =  salaryRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinSalary(NumberUtils.toInt(values[0]));
                    listForm.setMaxSalary(NumberUtils.toInt(values[1]));
                }
            }
        }

        if(listForm.getGpaRangeId() != null){
            DictVo gpaRange =  dictService.getById(listForm.getGpaRangeId());
            if(gpaRange != null){
                String[] values =  gpaRange.getValue().split("-");
                if(values.length == 2){
                    listForm.setMinGpa(NumberUtils.toDouble(values[0]));
                    listForm.setMaxGpa(NumberUtils.toDouble(values[1]));
                }
            }
        }

        ResumeOptions resumeOptions = new ResumeOptions();
        BeanUtil.copyProperties(listForm, resumeOptions);
        resumeOptions.setCategoryIds(FormUtils.joinWhereIds(listForm.getCategoryIds()));
        resumeOptions.setCityIds(FormUtils.joinWhereIds(listForm.getCityIds()));
        resumeOptions.setDegreeIds(FormUtils.joinWhereIds(listForm.getDegreeIds()));
        resumeOptions.setSchoolIds(FormUtils.joinWhereIds(listForm.getSchoolIds()));
        AppUtils.setPage(listForm);
        Page<Resume> resumePage = (Page<Resume>) resumeMapper.selectAndList(resumeOptions);
        PageResult<ResumeVo> pageResult = new PageResult<>(resumePage);
        List<ResumeVo> resumeVoList = new ArrayList<>(resumePage.size());
        for(Resume resume: resumePage){
            ResumeVo resumeVo = new ResumeVo().asVo(resume);
            List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
            resumeVo.setResumeEduList(resumeEduVoList);
            if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
                ResumeEduVo maxResumeEduVo = new ResumeEduVo();
                BeanUtil.copyProperties(resumeEduVoList.get(0), maxResumeEduVo);
                resumeVo.setMaxResumeEdu(maxResumeEduVo);
            }

            UserApplicantVo userVo = userService.getUserInfo(resume.getUserId());
            if(userVo != null){
                resumeVo.setEmail(userVo.getEmail());
                resumeVo.setPhone(userVo.getPhone());
                resumeVo.setPhoneCode(userVo.getPhoneCode());
            }

            resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(resume.getUserId()));

            //统计简历投递情况
            JobApplyOptions applyOptions = new JobApplyOptions();
            applyOptions.setResumeId(resume.getId());
            applyOptions.setUserId(listForm.getUserId());
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
            final int  applyOfferCount = jobApplyMapper.countJobApply(applyOptions);
            resumeVo.setApplyOfferCount(applyOfferCount);
            resumeVoList.add(resumeVo);
        }
        pageResult.setList(resumeVoList);
        return pageResult;
    }

    /**
     * 获取简历详情
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeDetail(Long resumeId){
        // 检查权限
        if(curUser() != null){
            if(curUser().getType() == UserType.COMPANY.value) {
                JobApplyOptions options = new JobApplyOptions();
                options.setCreatorId(curUser().getId());
                options.setResumeId(resumeId);
                final int count = jobApplyMapper.countJobApply(options);
                if (count == 0) {
                    throw new ServiceException(ApiCode.PERMISSION_DENIED);
                }
            }else if(curUser().getType() == UserType.GENERAL.value){
                checkResumeCreator(resumeId);
            }
        }
        ResumeVo resumeVo = getResumeInfo(resumeId);
        resumeVo.setResumePracticeList(resumePracticeService.getResumePracticeList(resumeId));
        resumeVo.setResumeLinkList(resumeLinkService.getResumeLinkList(resumeId));
        resumeVo.setResumeCompleteProgress(AppUtils.calCompleteProgress(resumeVo));
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
        resume.setIntroduction(resumeForm.getIntroduction());

        boolean updateFlag = false;
        if (StringUtils.isNotEmpty(resumeForm.getAttachResume())) {
            String newAttachResume = AppUtils.getOssKey(resumeForm.getAttachResume());
            //简历变更时更新索引
            if (!newAttachResume.equals(resume.getAttachResume())) updateFlag = true;
            resume.setAttachResume(newAttachResume);
        }
        if (resume.getId() == null) {
            resume.setId(AppUtils.nextId());
            resumeMapper.insertSelective(resume);
        } else {
            resume.setUpdateTime(new Date());
            resumeMapper.updateByPrimaryKeySelective(resume);
        }

        if (updateFlag) addOrUpdateResumeIndex(resume.getId(), resume.getUserId(), resumeForm.getAttachResume());

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
     * 删除附件简历
     *
     * @param resumeId
     */
    public void delResumeAttachment(Long resumeId){
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if (resume != null) {
            checkResumeCreator(resume);
            resume.setAttachResume("");
            resumeMapper.updateByPrimaryKey(resume);

            //简历索引删除
            deleteResumeIndex(resumeId);
        }
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
        UserApplicantVo toUser = userService.getUserInfo(resume.getUserId());
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
    public ResumeVo getResumeInfo(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        return toResumeVo(resume);
    }

    private ResumeVo toResumeVo(Resume resume) {
        ResumeVo resumeVo = new ResumeVo().asVo(resume);
        UserApplicantVo userVo = userService.getUserInfo(resume.getUserId());
        if(userVo != null){
            resumeVo.setAvatar(AppUtils.absOssUrl(userVo.getAvatar()));
            resumeVo.setEmail(userVo.getEmail());
            resumeVo.setPhoneCode(userVo.getPhoneCode());
            resumeVo.setPhone(userVo.getPhone());
        }
        List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
        resumeVo.setResumeEduList(resumeEduVoList);
        resumeVo.setResumeSkillList(resumeSkillService.getResumeSkillList(resume.getId()));
        resumeVo.setResumeExpList(resumeExpService.getResumeExpList(resume.getId()));
        if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
            ResumeEduVo maxResumeEduVo = JSON.parseObject(JSON.toJSONString(resumeEduVoList.get(0)), ResumeEduVo.class);
            resumeVo.setMaxResumeEdu(maxResumeEduVo);
        }
        return resumeVo;
    }

    private void checkResumeCreator(Resume resume) {
        if (resume != null && resume.getUserId() != null && !resume.getUserId().equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
    }

    /**
     * 添加或更新附件简历索引
     *
     * @param resumeId     简历id
     * @param attachResume 附件文件路径. 当前为阿里云oss http链接
     */
    @Async
    public void addOrUpdateResumeIndex(Long resumeId, Long userId, String attachResume) {
        try {
            //region 读取附件简历内容
            String suffix = attachResume.substring(attachResume.lastIndexOf(".") + 1);
            ResumeFileRead fileRead = resumeFileReadFactory.getFileRead(suffix);
            String attachContent = fileRead.read(new URL(attachResume));
            if (StringUtils.isBlank(attachContent)) return;
            //endregion

            //region 附件简历内容保存到数据库
            ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeId(resumeId);
            if (resumeAttach != null) {
                resumeAttach.setUpdateTime(new Date());
                resumeAttach.setAttachContent(attachContent);

                resumeAttachMapper.updateByPrimaryKeyWithBLOBs(resumeAttach);
            } else {
                resumeAttach = new ResumeAttach();
                resumeAttach.setResumeId(resumeId);
                resumeAttach.setUserId(userId);
                resumeAttach.setAttachContent(attachContent);

                resumeAttachMapper.insert(resumeAttach);
            }
            //endregion

            //TODO 索引更新
            Document document = new Document();

            //MQ广播索引更新指令
            rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.CreateOrUpdate, BusinessType.AttachResume));

        } catch (Exception e) {
            log.error("添加或更新附件简历索引时异常", e);
        }
    }

    /**
     * 删除简历附件索引
     *
     * @param resumeId 简历id
     */
    @Async
    public void deleteResumeIndex(Long resumeId) {

        ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeId(resumeId);
        if (resumeAttach != null) resumeAttachMapper.deleteByPrimaryKey(resumeAttach.getId());

        //TODO 索引删除
        Document document = new Document();

        //MQ广播索引更新指令
        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.Delete, BusinessType.AttachResume));
    }
}
