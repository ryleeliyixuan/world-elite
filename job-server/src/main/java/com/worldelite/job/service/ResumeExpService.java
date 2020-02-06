package com.worldelite.job.service;

import com.worldelite.job.entity.ResumeEdu;
import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.form.ResumeExpForm;
import com.worldelite.job.mapper.ResumeExperienceMapper;
import com.worldelite.job.vo.ResumeExpVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumeExpService extends BaseService{

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeExperienceMapper resumeExpMapper;

    /**
     * 保存实践经验
     *
     * @param resumeExpForm
     * @return
     */
    public ResumeExpVo saveResumeExp(ResumeExpForm resumeExpForm){
        resumeService.checkResumeCreator(resumeExpForm.getResumeId());

        ResumeExperience resumeExp = null;
        if(resumeExpForm.getId() != null){
            resumeExp = resumeExpMapper.selectByPrimaryKey(resumeExpForm.getId());
        }

        if(resumeExp == null){
            resumeExp = new ResumeExperience();
            resumeExp.setResumeId(resumeExpForm.getResumeId());
        }

        resumeExp.setCompany(resumeExpForm.getCompany());
        resumeExp.setPosition(resumeExpForm.getPosition());
        resumeExp.setStartDate(resumeExpForm.getStartDate());
        resumeExp.setFinishDate(resumeExpForm.getFinishDate());
        resumeExp.setDescription(resumeExpForm.getDescription());

        if(resumeExp.getId() == null){
            resumeExpMapper.insertSelective(resumeExp);
        }else{
            resumeExp.setUpdateTime(new Date());
            resumeExpMapper.updateByPrimaryKeySelective(resumeExp);
        }

        return new ResumeExpVo().asVo(resumeExp);
    }


}
