package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.ResumeEdu;
import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.form.ResumeExpForm;
import com.worldelite.job.mapper.ResumeExperienceMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeEduVo;
import com.worldelite.job.vo.ResumeExpVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

        BeanUtils.copyProperties(resumeExpForm, resumeExp);

        if(resumeExp.getId() == null){
            resumeExpMapper.insertSelective(resumeExp);
        }else{
            resumeExp.setUpdateTime(new Date());
            resumeExpMapper.updateByPrimaryKeySelective(resumeExp);
        }

        return new ResumeExpVo().asVo(resumeExp);
    }

    /**
     * 删除工作经验
     *
     * @param id
     */
    public void deleteResumeExp(Integer id){
        ResumeExperience resumeExp = resumeExpMapper.selectByPrimaryKey(id);
        if(resumeExp != null){
            resumeService.checkResumeCreator(resumeExp.getResumeId());
            resumeExpMapper.deleteByPrimaryKey(resumeExp.getId());
        }
    }

    /**
     * 获取工作经验列表
     *
     * @param resumeId
     * @return
     */
    public List<ResumeExpVo> getResumeExpList(Long resumeId){
        ResumeExperience options = new ResumeExperience();
        options.setResumeId(resumeId);
        PageHelper.orderBy("position desc");
        List<ResumeExperience> resumeExpList = resumeExpMapper.selectAndList(options);
        return AppUtils.asVoList(resumeExpList, ResumeExpVo.class);
    }
}
