package com.worldelite.job.service;

import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.entity.ResumeSkill;
import com.worldelite.job.form.ResumeSkillForm;
import com.worldelite.job.mapper.ResumeSkillMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeSkillVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumeSkillService extends BaseService{

    @Autowired
    @Lazy
    private ResumeService resumeService;

    @Autowired
    private ResumeSkillMapper resumeSkillMapper;

    /**
     * 保存简历擅长技能
     *
     * @param resumeSkillForm
     * @return
     */
    public List<ResumeSkillVo> saveResumeSkill(ResumeSkillForm resumeSkillForm){
//        resumeService.checkResumeCreator(resumeSkillForm.getResumeId());
        resumeSkillMapper.deleteByResumeId(resumeSkillForm.getResumeId());
        if(ArrayUtils.isNotEmpty(resumeSkillForm.getTagList())){
            for(String skillTag: resumeSkillForm.getTagList()){
                ResumeSkill resumeSkill = new ResumeSkill();
                resumeSkill.setName(skillTag);
                resumeSkill.setResumeId(resumeSkillForm.getResumeId());
                resumeSkillMapper.insertSelective(resumeSkill);
            }
        }
        return getResumeSkillVoList(resumeSkillForm.getResumeId());
    }

    /**
     * 获取简历技能列表
     *
     * @param resumeId
     * @return
     */
    public List<ResumeSkillVo> getResumeSkillVoList(Long resumeId){
        ResumeSkill options = new ResumeSkill();
        options.setResumeId(resumeId);
        List<ResumeSkill> resumeSkillList = resumeSkillMapper.selectAndList(options);
        return AppUtils.asVoList(resumeSkillList, ResumeSkillVo.class);
    }

    public List<ResumeSkill> getResumeSkillList(Long resumeId){
        ResumeSkill options = new ResumeSkill();
        options.setResumeId(resumeId);
        return resumeSkillMapper.selectAndList(options);
    }
}
