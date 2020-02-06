package com.worldelite.job.service;

import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.entity.ResumeSkill;
import com.worldelite.job.form.ResumeSkillForm;
import com.worldelite.job.mapper.ResumeSkillMapper;
import com.worldelite.job.vo.ResumeSkillVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumeSkillService extends BaseService{

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeSkillMapper resumeSkillMapper;

    /**
     * 保存简历擅长技能
     *
     * @param resumeSkillForm
     * @return
     */
    public ResumeSkillVo saveResumeSkill(ResumeSkillForm resumeSkillForm){
        resumeService.checkResumeCreator(resumeSkillForm.getResumeId());

        ResumeSkill resumeSkill = null;
        if(resumeSkillForm.getId() != null){
            resumeSkill = resumeSkillMapper.selectByPrimaryKey(resumeSkillForm.getId());
        }

        if(resumeSkill == null){
            resumeSkill = new ResumeSkill();
            resumeSkill.setResumeId(resumeSkillForm.getResumeId());
        }

        resumeSkill.setName(resumeSkillForm.getName());
        resumeSkill.setLevel(resumeSkillForm.getLevel());

        if(resumeSkill.getId() == null){
            resumeSkillMapper.insertSelective(resumeSkill);
        }else{
            resumeSkill.setUpdateTime(new Date());
            resumeSkillMapper.updateByPrimaryKeySelective(resumeSkill);
        }

        return new ResumeSkillVo().asVo(resumeSkill);
    }
}
