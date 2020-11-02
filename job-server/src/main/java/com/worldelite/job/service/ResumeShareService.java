package com.worldelite.job.service;

import com.worldelite.job.entity.CompanyUser;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeShare;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResumeShareForm;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.ResumeShareMapper;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.util.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 简历分享服务类
 */
@Slf4j
@Service
public class ResumeShareService extends BaseService{

    @Autowired
    private ResumeShareMapper resumeShareMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    /**
     * 分享简历
     * @param resumeShareForm
     */
    @Transactional
    public void share(ResumeShareForm resumeShareForm){
        //复制简历数据到企业简历库
        Long resumeId = resumeShareForm.getResumeId();
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
        Resume resume = resumeService.getResumeBasic(resumeId);
        resume.setId(AppUtils.nextId());
        resumeService.saveBasic(null);
        //往分享表添加数据
        Long fromId = getCompanyId(curUser().getId());
        ResumeShare resumeShare = new ResumeShare();
        resumeShare.setFromId(fromId);
        resumeShare.setToId(resumeShareForm.getToId());
        resumeShare.setOriginId(resumeShareForm.getResumeId());
        resumeShare.setResumeId(resume.getId());
        resumeShareMapper.insertSelective(resumeShare);
        //生成索引
        resumeService.saveResumeItem(resume.getId());
    }

    private Long getCompanyId(Long userId){
        CompanyUser companyUser = companyUserMapper.selectByUserId(userId);
        if(companyUser==null){
            throw new ServiceException(message("api.error.data.company"));
        }
        return companyUser.getCompanyId();
    }

}
