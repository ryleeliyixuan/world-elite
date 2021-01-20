package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeLanguage;
import com.worldelite.job.entity.ResumeMergeAttach;
import com.worldelite.job.form.AttachOtherForm;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeLanguageForm;
import com.worldelite.job.mapper.ResumeLanguageMapper;
import com.worldelite.job.mapper.ResumeMergeAttachMapper;
import com.worldelite.job.util.AppUtils;
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
public class ResumeMergeAttachService{

    @Autowired
    private ResumeMergeAttachMapper resumeMergeAttachMapper;


    public void saveResumeMergeAttaches(List<AttachOtherForm>others, Resume resume){
        if (others==null) {
            return;
        }

        //delete old others
        deleteResumeMergeAttaches(resume);
        for (AttachOtherForm oneOther: others){
            //add new others
            ResumeMergeAttach record = new ResumeMergeAttach();
            record.setResumeAttach(AppUtils.getOssKey(oneOther.getLink()));
            record.setResumeId(resume.getId());
            record.setName(oneOther.getName());
            resumeMergeAttachMapper.insertSelective(record);
        }
    }


    public void deleteResumeMergeAttaches(Resume resume){
        ResumeMergeAttach options = new ResumeMergeAttach();
        options.setResumeId(resume.getId());
        List<ResumeMergeAttach> lists = resumeMergeAttachMapper.selectAndList(options);
        for (ResumeMergeAttach list:lists){
            resumeMergeAttachMapper.deleteByPrimaryKey(list.getId());
        }
    }

    public List<ResumeMergeAttach>getResumeMergeAttaches(Long resumeId){
        ResumeMergeAttach options = new ResumeMergeAttach();
        options.setResumeId(resumeId);
        return resumeMergeAttachMapper.selectAndList(options);
    }

}
