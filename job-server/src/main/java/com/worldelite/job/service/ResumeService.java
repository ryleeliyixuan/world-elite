package com.worldelite.job.service;

import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.User;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.UserForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ResumeVo;
import com.worldelite.job.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collection;
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
        }else{
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
     * 检查简历创建者是否登录用户
     *
     * @param resumeId
     */
    public void checkResumeCreator(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        checkResumeCreator(resume);
    }

    private void checkResumeCreator(Resume resume) {
        if (resume != null && resume.getUserId() != null && !resume.getUserId().equals(curUser().getId())) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
    }
}
