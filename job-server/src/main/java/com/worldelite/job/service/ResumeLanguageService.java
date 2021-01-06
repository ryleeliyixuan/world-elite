package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.entity.ResumeLanguage;
import com.worldelite.job.form.ResumeExpForm;
import com.worldelite.job.form.ResumeLanguageForm;
import com.worldelite.job.mapper.ResumeExperienceMapper;
import com.worldelite.job.mapper.ResumeLanguageMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeExpVo;
import com.worldelite.job.vo.ResumeLanguageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Nuo Xu
 */
@Service
public class ResumeLanguageService extends BaseService{

    @Autowired
    private ResumeLanguageMapper resumeLanguageMapper;


    public ResumeLanguageVo saveResumeLanguage(ResumeLanguageForm resumeLanguageForm){

        ResumeLanguage resumeLanguage = null;
        if(resumeLanguageForm.getId() != null){
            resumeLanguage = resumeLanguageMapper.selectByPrimaryKey(resumeLanguageForm.getId());

        }

        if(resumeLanguage == null){
           resumeLanguage = new ResumeLanguage();
            resumeLanguage.setResumeId(resumeLanguage.getResumeId());
        }

        BeanUtils.copyProperties(resumeLanguageForm, resumeLanguage);

        if(resumeLanguage.getId() == null){
            resumeLanguageMapper.insertSelective(resumeLanguage);
        }else{
            resumeLanguage.setUpdateTime(new Date());
            resumeLanguageMapper.updateByPrimaryKeySelective(resumeLanguage);
        }

        return new ResumeLanguageVo().asVo(resumeLanguage);
    }


    public void deleteResumeLanguage(Integer id){
        ResumeLanguage resumeLanguage = resumeLanguageMapper.selectByPrimaryKey(id);
        if(resumeLanguage != null){
            resumeLanguageMapper.deleteByPrimaryKey(resumeLanguage.getId());
        }
    }


    public List<ResumeLanguageVo> getResumeLanguageVoList(Long resumeId){
        ResumeLanguage options = new ResumeLanguage();
        options.setResumeId(resumeId);
//        PageHelper.orderBy("position desc");
        List<ResumeLanguage> resumeLanguageList = resumeLanguageMapper.selectAndList(options);
        return AppUtils.asVoList(resumeLanguageList, ResumeLanguageVo.class);
    }

    public List<ResumeLanguage> getResumeLanguageList(Long resumeId){
        ResumeLanguage options = new ResumeLanguage();
        options.setResumeId(resumeId);
//        PageHelper.orderBy("position desc");
        return resumeLanguageMapper.selectAndList(options);
    }
}
