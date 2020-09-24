package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.JobApplyStatus;
import com.worldelite.job.constants.JobApplyType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResumeEduForm;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeListForm;
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
        resumeOptions.setExtQuery("id in (select resume_id from t_resume_repository where company_id = '"+getCompanyId()+"')");
        resumeOptions.setCategoryIds(FormUtils.joinWhereIds(listForm.getCategoryIds()));
        resumeOptions.setCityIds(FormUtils.joinWhereIds(listForm.getCityIds()));
        resumeOptions.setDegreeIds(FormUtils.joinWhereIds(listForm.getDegreeIds()));
        resumeOptions.setSchoolIds(FormUtils.joinWhereIds(listForm.getSchoolIds()));
        AppUtils.setPage(listForm);
        Page<Resume> resumePage = (Page<Resume>) resumeService.selectAndList(resumeOptions);
        PageResult<ResumeVo> pageResult = new PageResult<>(resumePage);
        List<ResumeVo> resumeVoList = new ArrayList<>(resumePage.size());
        for(Resume resume: resumePage){
            ResumeRepository resumeRepository = resumeRepositoryMapper.selectByResumeId(resume.getId());
            if(resumeRepository == null){
                throw new ServiceException("简历缺少用户信息");
            }
            Long userId = resumeRepository.getUserId();
            UserRepository userRepository = userRepositoryMapper.selectByPrimaryKey(userId);
            ResumeVo resumeVo = toResumeVo(resume,userRepository);

            List<ResumeEduVo> resumeEduVoList = resumeEduService.getResumeEduList(resume.getId());
            resumeVo.setResumeEduList(resumeEduVoList);
            if (CollectionUtils.isNotEmpty(resumeEduVoList)) {
                ResumeEduVo maxResumeEduVo = new ResumeEduVo();
                BeanUtil.copyProperties(resumeEduVoList.get(0), maxResumeEduVo);
                resumeVo.setMaxResumeEdu(maxResumeEduVo);
            }

            resumeVo.setUserExpectJob(userExpectJobService.getUserExpectJob(userId));

            //统计简历投递情况
            int applyOfferCount = getApplyOfferCount(resume.getId(),userId,resumeVo);
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
    public ResumeVo getResumeDetail(Long resumeId) {
        ResumeVo resumeVo = resumeService.getResumeDetail(resumeId);
        ResumeRepository resumeRepository = resumeRepositoryMapper.selectByResumeId(resumeId);
        UserRepository userRepository = userRepositoryMapper.selectByPrimaryKey(resumeRepository.getUserId());
        resumeVo.setUserId(String.valueOf(userRepository.getId()));
        resumeVo.setAvatar(AppUtils.absOssUrl(userRepository.getAvatar()));
        resumeVo.setEmail(userRepository.getEmail());
        if(userRepository.getPhone() != null && userRepository.getPhone() != 0){
            resumeVo.setPhone(String.valueOf(userRepository.getPhone()));
        }
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
        Resume resume = resumeService.saveBasic(resumeForm);
        //保存或者更新用户信息
        ResumeRepository resumeRepository = resumeRepositoryMapper.selectByResumeId(resume.getId());
        UserRepository userRepository = new UserRepository();
        if(resumeRepository == null){
            addOrUpdateUserRepository(userRepository,resumeForm);
            addResumeRepository(userRepository.getId(),resume.getId());
        }else{
            userRepository = userRepositoryMapper.selectByPrimaryKey(resumeRepository.getUserId());
            addOrUpdateUserRepository(userRepository,resumeForm);
        }
        //Todo 创建索引
        return toResumeVo(resume,userRepository);
    }

    /**
     * 删除附件简历
     *
     * @param resumeId
     */
    public void delResumeAttachment(Long resumeId){
        //Todo 做一些权限检查
        resumeService.delResumeAttachment(resumeId);
        //Todo 更新索引
    }

    public void addToJobApply(Long resumeId,Long jobId,Byte status){
        JobApply newJobApply = new JobApply();
        ResumeRepository resumeRepository = resumeRepositoryMapper.selectByResumeId(resumeId);
        //Todo 现在企业简历未关联职位
        newJobApply.setJobId(jobId);
        newJobApply.setUserId(resumeRepository.getUserId());
        newJobApply.setStatus(status);
        newJobApply.setType(JobApplyType.REPOSITORY.value);
        newJobApply.setResumeId(resumeRepository.getResumeId());
        jobApplyMapper.insertSelective(newJobApply);
    }

    /**
     * 获取当前登录用户对应的企业ID
     * @return
     */
    private Long getCompanyId(){
        Long userId = curUser().getId();
        CompanyUser companyUser = companyUserMapper.selectByUserId(userId);
        if(companyUser == null){
            throw new ServiceException("用户未关联企业");
        }
        return companyUser.getCompanyId();
    }

    private void addOrUpdateUserRepository(UserRepository userRepository,ResumeForm resumeForm){
        userRepository.setAvatar(AppUtils.getOssKey(resumeForm.getAvatar()));
        userRepository.setName(resumeForm.getName());
        userRepository.setGender(resumeForm.getGender());
        userRepository.setPhoneCode(resumeForm.getPhoneCode());
        userRepository.setPhone(resumeForm.getPhone());
        userRepository.setEmail(resumeForm.getEmail());
        if(userRepository.getId() == null){
            userRepository.setId(AppUtils.nextId());
            userRepositoryMapper.insertSelective(userRepository);
        }else{
            userRepositoryMapper.updateByPrimaryKeySelective(userRepository);
        }
    }

    private void addResumeRepository(Long userId,Long resumeId){
        ResumeRepository resumeRepository = new ResumeRepository();
        resumeRepository.setUserId(userId);
        resumeRepository.setResumeId(resumeId);
        resumeRepository.setCompanyId(getCompanyId());
        resumeRepository.setCreatorId(curUser().getId());
        resumeRepositoryMapper.insertSelective(resumeRepository);
    }

    private ResumeVo toResumeVo(Resume resume,UserRepository userRepository){
        ResumeVo resumeVo = new ResumeVo().asVo(resume);
        if(userRepository == null) return resumeVo;
        resumeVo.setUserId(String.valueOf(userRepository.getId()));
        resumeVo.setAvatar(AppUtils.absOssUrl(userRepository.getAvatar()));
        resumeVo.setEmail(userRepository.getEmail());
        if(userRepository.getPhone() != null && userRepository.getPhone() != 0){
            resumeVo.setPhone(String.valueOf(userRepository.getPhone()));
        }
        return resumeVo;
    }

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
    public void saveAttachment(String attachmentName) {
        ResumeVo resumeVo = resumeSDK.parse(attachmentName);
        resumeVo.setAttachResume(AppUtils.getOssKey(attachmentName));
        //保存基本信息
        ResumeForm resumeForm = getResumeForm(resumeVo);
        ResumeVo savedResume = saveBasic(resumeForm);
        Long resumeId = Long.parseLong(savedResume.getId());
        Long userId = Long.parseLong(savedResume.getUserId());
        //保存教育信息
        List<ResumeEduVo> resumeEduVoList = resumeVo.getResumeEduList();
        for(ResumeEduVo resumeEduVo:resumeEduVoList){
            ResumeEduForm resumeEduForm = getResumeEduForm(resumeEduVo);
            resumeEduForm.setResumeId(resumeId);
            resumeEduForm.setUserId(userId);
            resumeEduService.saveResumeEdu(resumeEduForm);
        }
    }

    private ResumeForm getResumeForm(ResumeVo resumeVo){
        ResumeForm resumeForm = new ResumeForm();
        resumeForm.setName(resumeVo.getName());
        resumeForm.setBirth(resumeVo.getBirth());
        resumeForm.setGender(resumeVo.getGender());
        resumeForm.setGraduateTime(resumeVo.getGraduateTime());
        resumeForm.setCurPlace(resumeVo.getCurPlace());
        resumeForm.setIntroduction(resumeVo.getIntroduction());
        resumeForm.setEmail(resumeVo.getEmail());
        try {
            resumeForm.setPhone(Long.parseLong(resumeVo.getPhone()));
        }catch (Exception e){

        }
        resumeForm.setAvatar(resumeVo.getAvatar());
        resumeForm.setAttachResume(resumeVo.getAttachResume());
        return resumeForm;
    }

    private ResumeEduForm getResumeEduForm(ResumeEduVo resumeEduVo){
        ResumeEduForm resumeEduForm = new ResumeEduForm();
        resumeEduForm.setStartTime(resumeEduVo.getStartTime());
        resumeEduForm.setFinishTime(resumeEduVo.getFinishTime());
        resumeEduForm.setSchoolName(resumeEduVo.getSchoolName());
        resumeEduForm.setMajorName(resumeEduVo.getMajorName());
        resumeEduForm.setGpa(resumeEduVo.getGpa());
        return resumeEduForm;
    }
}
