package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.ResumeCertificate;
import com.worldelite.job.form.ResumeCertificateForm;
import com.worldelite.job.mapper.ResumeCertificateMapper;
import com.worldelite.job.mapper.ResumeCertificateMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeCertificateVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Nuo Xu
 */
@Service
public class ResumeCertificateService extends BaseService{

    @Autowired
    private ResumeCertificateMapper resumeCertificateMapper;


    public ResumeCertificateVo saveResumeCertificate(ResumeCertificateForm resumeCertificateForm){

        ResumeCertificate resumeCertificate = null;
        if(resumeCertificateForm.getId() != null){
            resumeCertificate = resumeCertificateMapper.selectByPrimaryKey(resumeCertificateForm.getId());

        }

        if(resumeCertificate == null){
           resumeCertificate = new ResumeCertificate();
            resumeCertificate.setResumeId(resumeCertificate.getResumeId());
        }

        BeanUtils.copyProperties(resumeCertificateForm, resumeCertificate);

        if(resumeCertificate.getId() == null){
            resumeCertificateMapper.insertSelective(resumeCertificate);
        }else{
            resumeCertificate.setUpdateTime(new Date());
            resumeCertificateMapper.updateByPrimaryKeySelective(resumeCertificate);
        }

        return new ResumeCertificateVo().asVo(resumeCertificate);
    }


    public void deleteResumeCertificate(Integer id){
        ResumeCertificate resumeCertificate = resumeCertificateMapper.selectByPrimaryKey(id);
        if(resumeCertificate != null){
            resumeCertificateMapper.deleteByPrimaryKey(resumeCertificate.getId());
        }
    }


    public List<ResumeCertificateVo> getResumeCertificateVoList(Long resumeId){
        ResumeCertificate options = new ResumeCertificate();
        options.setResumeId(resumeId);
//        PageHelper.orderBy("position desc");
        List<ResumeCertificate> resumeCertificateList = resumeCertificateMapper.selectAndList(options);
        return AppUtils.asVoList(resumeCertificateList, ResumeCertificateVo.class);
    }

    public List<ResumeCertificate> getResumeCertificateList(Long resumeId){
        ResumeCertificate options = new ResumeCertificate();
        options.setResumeId(resumeId);
//        PageHelper.orderBy("position desc");
        return resumeCertificateMapper.selectAndList(options);
    }
}
