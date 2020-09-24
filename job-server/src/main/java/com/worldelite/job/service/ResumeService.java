package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.UserForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeEduVo;
import com.worldelite.job.vo.ResumeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class ResumeService extends BaseService {

    @Autowired
    private ResumeMapper resumeMapper;

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

    /**
     * 新增简历
     * @param resume
     */
    public void insertSelective(Resume resume){
        resumeMapper.insertSelective(resume);
    }

    /**
     * 通过简历ID获取简历数据
     * @param resumeId
     * @return
     */
    public Resume selectByPrimaryKey(Long resumeId){
        return resumeMapper.selectByPrimaryKey(resumeId);
    }

    /**
     * 通过筛选条件查询简历数据
     * @param resume
     * @return
     */
    public List<Resume> selectAndList(Resume resume){
        return resumeMapper.selectAndList(resume);
    }

    /**
     * 创建只有姓名的空简历
     * @param name
     * @return
     */
    public Resume createResume(String name){
        Resume resume = new Resume();
        resume.setId(AppUtils.nextId());
        resume.setName(name);
        resumeMapper.insertSelective(resume);
        return resume;
    }

    public Future<Boolean> addOrUpdateResumeIndex(Long resumeId, Long userId, String s) {
        return null;
    }

    /**
     * 获取简历详情
     *
     * @param resumeId
     * @return
     */
    public ResumeVo getResumeDetail(Long resumeId){
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
    public Resume saveBasic(ResumeForm resumeForm) {
        //如果参数中有简历ID了，则从数据库取出简历数据
        Resume resume = null;
        if (resumeForm.getId() != null) {
            resume = resumeMapper.selectByPrimaryKey(resumeForm.getId());
        }
        if (resume == null) {
            resume = new Resume();
        }
        //将表格中的参数保存到Resume对象
        resume.setName(resumeForm.getName());
        resume.setBirth(resumeForm.getBirth());
        resume.setGender(resumeForm.getGender());
        resume.setCountryId(resumeForm.getCountryId());
        resume.setCurPlace(resumeForm.getCurPlace());
        resume.setGraduateTime(resumeForm.getGraduateTime());
        resume.setReturnTime(resumeForm.getReturnTime());
        resume.setMaxDegreeId(resumeForm.getMaxDegreeId());
        resume.setIntroduction(resumeForm.getIntroduction());
        if (StringUtils.isNotEmpty(resumeForm.getAttachResume())) {
            String newAttachResume = AppUtils.getOssKey(resumeForm.getAttachResume());
            resume.setAttachResume(newAttachResume);
        }
        //新增或者更新简历数据到数据库
        if (resume.getId() == null) {
            resume.setId(AppUtils.nextId());
            resumeMapper.insertSelective(resume);
        } else {
            resume.setUpdateTime(new Date());
            resumeMapper.updateByPrimaryKeySelective(resume);
        }
        return resume;
    }

    /**
     * 删除附件简历
     *
     * @param resumeId
     */
    public void delResumeAttachment(Long resumeId){
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if (resume != null) {
            resume.setAttachResume("");
            resumeMapper.updateByPrimaryKey(resume);
        }
    }

//    /**
//     * 处理简历
//     */
//    public void handleApplyResume(JobApplyForm applyResumeForm) {
//        JobApply jobApply = jobApplyMapper.selectByPrimaryKey(applyResumeForm.getId());
//        if (jobApply == null) {
//            throw new ServiceException(String.valueOf(applyResumeForm.getId()), ApiCode.OBJECT_NOT_FOUND);
//        }
//
//        Resume resume = resumeMapper.selectByPrimaryKey(jobApply.getResumeId());
//        if (resume == null) {
//            throw new ServiceException(String.valueOf(applyResumeForm.getId()), ApiCode.OBJECT_NOT_FOUND);
//        }
//
//        JobVo job = jobService.getJobInfo(jobApply.getJobId(), true);
//        if (!job.getCreatorId().equals(curUser().getId())) {
//            throw new ServiceException(ApiCode.PERMISSION_DENIED);
//        }
//
//        jobApply.setStatus(applyResumeForm.getStatus());
//        jobApplyMapper.updateByPrimaryKeySelective(jobApply);
//
//        if(applyResumeForm.getStatus() == JobApplyStatus.VIEW.value){
//            return;
//        }
//
//        // 发送站内和邮件消息
//        UserApplicantVo toUser = userService.getUserInfo(resume.getUserId());
//        final String jobPlaceholder = String.format("%s.%s", job.getCompanyUser().getCompany().getName(), job.getName());
//        EmailForm emailForm = null;
//        String messageContent = null;
//        if (applyResumeForm.getStatus() == JobApplyStatus.CANDIDATE.value) {
//            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_CANDIDATE);
//            messageContent = message("message.apply.candidate", jobPlaceholder);
//        } else if (applyResumeForm.getStatus() == JobApplyStatus.INTERVIEW.value) {
//            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_INTERVIEW);
//            messageContent = message("message.apply.interview", jobPlaceholder);
//        } else if (applyResumeForm.getStatus() == JobApplyStatus.OFFER.value) {
//            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_OFFER);
//            messageContent = message("message.apply.offer", jobPlaceholder);
//        } else if (applyResumeForm.getStatus() == JobApplyStatus.ABANDON.value) {
//            emailForm = configService.getEmailForm(ConfigType.EMAIL_JOB_APPLY_ABANDON);
//            messageContent = message("message.apply.abandon", jobPlaceholder);
//        }
//
//        if (emailForm != null) {
//            final String emailBody = emailForm.getEmailBody().replace("${JOB}", jobPlaceholder).replace("${DETAIL_URL}", AppUtils.wholeWebUrl("/apply-jobs" ));
//            emailForm.setEmailBody(emailBody);
//            emailForm.setAddress(toUser.getEmail());
//            emailService.sendEmail(emailForm);
//        }
//
//        if(messageContent != null){
//            Message message = new Message();
//            message.setFromUser(curUser().getId());
//            message.setToUser(resume.getUserId());
//            message.setContent(messageContent);
//            messageService.sendMessage(message);
//        }
//    }
//
//    /**
//     * 检查简历创建者是否登录用户
//     *
//     * @param resumeId
//     */
//    public void checkResumeCreator(Long resumeId) {
//        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
//        checkResumeCreator(resume);
//    }
//
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
//
//    private void checkResumeCreator(Resume resume) {
//        if (resume != null && resume.getUserId() != null && !resume.getUserId().equals(curUser().getId())) {
//            throw new ServiceException(ApiCode.PERMISSION_DENIED);
//        }
//    }
//
//    /**
//     * 添加或更新附件简历索引
//     *
//     * @param resumeId     简历id
//     * @param attachResume 附件文件路径. 当前为阿里云oss http链接
//     */
//    @Async
//    public Future<Boolean> addOrUpdateResumeIndex(Long resumeId, Long userId, String attachResume) {
//        Document document = null;
//        try {
//            //region 读取附件简历内容
//            String suffix = attachResume.substring(attachResume.lastIndexOf(".") + 1);
//            ResumeFileRead fileRead = resumeFileReadFactory.getFileRead(suffix);
//            String attachContent = fileRead.read(new URL(attachResume));
//            if (StringUtils.isBlank(attachContent)) return new AsyncResult<>(false);
//            //endregion
//
//            //region 附件简历内容保存到数据库
//            ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeId(resumeId);
//            if (resumeAttach != null) {
//                resumeAttach.setUpdateTime(new Date());
//                resumeAttach.setAttachContent(attachContent);
//
//                resumeAttachMapper.updateByPrimaryKeyWithBLOBs(resumeAttach);
//                document = resumeAttachService.updateIndex(resumeAttach);
//            } else {
//                resumeAttach = new ResumeAttach();
//                resumeAttach.setResumeId(resumeId);
//                resumeAttach.setUserId(userId);
//                resumeAttach.setAttachContent(attachContent);
//
//                resumeAttachMapper.insert(resumeAttach);
//                document = resumeAttachService.appendIndex(resumeAttach);
//            }
//            //endregion
//
//            //MQ广播索引更新指令
//            rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.CreateOrUpdate, BusinessType.AttachResume));
//
//        } catch (Exception e) {
//            log.error("添加或更新附件简历索引时异常", e);
//            return new AsyncResult<>(false);
//        }
//
//        return new AsyncResult<>(true);
//    }
//
//    /**
//     * 删除简历附件索引
//     *
//     * @param resumeId 简历id
//     */
//    @Async
//    public Future<Boolean> deleteResumeIndex(Long resumeId) {
//
//        Document document = null;
//        ResumeAttach resumeAttach = resumeAttachMapper.selectByResumeId(resumeId);
//        if (resumeAttach != null) {
//            resumeAttachMapper.deleteByPrimaryKey(resumeAttach.getId());
//            document = resumeAttachService.deleteIndex(resumeAttach.getResumeId());
//        }
//
//        //MQ广播索引更新指令
//        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.Delete, BusinessType.AttachResume));
//        return new AsyncResult<>(true);
//    }
}
