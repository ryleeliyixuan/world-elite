package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.ResumeExperience;
import com.worldelite.job.entity.ResumePractice;
import com.worldelite.job.form.ResumePracticeForm;
import com.worldelite.job.mapper.ResumePracticeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeExpVo;
import com.worldelite.job.vo.ResumePracticeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumePracticeService extends BaseService{

    @Autowired
    private ResumePracticeMapper practiceMapper;

    @Autowired
    @Lazy
    private ResumeService resumeService;

    /**
     * 保存实践经验
     * @param practiceForm
     * @return
     */
    public ResumePracticeVo saveResumePractice(ResumePracticeForm practiceForm){
//        resumeService.checkResumeCreator(practiceForm.getResumeId());

        ResumePractice resumePractice = null;
        if(practiceForm.getId() != null){
            resumePractice = practiceMapper.selectByPrimaryKey(practiceForm.getId());
        }
        if(resumePractice == null){
            resumePractice = new ResumePractice();
            resumePractice.setResumeId(practiceForm.getResumeId());
        }

        BeanUtils.copyProperties(practiceForm, resumePractice);

        if(resumePractice.getId() == null){
            practiceMapper.insertSelective(resumePractice);
        }else{
            resumePractice.setUpdateTime(new Date());
            practiceMapper.updateByPrimaryKeySelective(resumePractice);
        }

        return new ResumePracticeVo().asVo(resumePractice);
    }

    /**
     * 删除工作经验
     *
     * @param id
     */
    public void deleteResumePractice(Integer id){
        ResumePractice practice = practiceMapper.selectByPrimaryKey(id);
        if(practice != null){
//            resumeService.checkResumeCreator(practice.getResumeId());
            practiceMapper.deleteByPrimaryKey(practice.getId());
        }
    }

    /**
     * 获取经验列表
     *
     * @param resumeId
     * @return
     */
    public List<ResumePracticeVo> getResumePracticeVoList(Long resumeId){
        ResumePractice options = new ResumePractice();
        options.setResumeId(resumeId);
        PageHelper.orderBy("position desc");
        List<ResumePractice> resumePracticeList = practiceMapper.selectAndList(options);
        return AppUtils.asVoList(resumePracticeList, ResumePracticeVo.class);
    }

    public List<ResumePractice> getResumePracticeList(Long resumeId){
        ResumePractice options = new ResumePractice();
        options.setResumeId(resumeId);
        PageHelper.orderBy("position desc");
        return practiceMapper.selectAndList(options);
    }
}
