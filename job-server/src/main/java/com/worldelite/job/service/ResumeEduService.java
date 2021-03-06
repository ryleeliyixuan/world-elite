package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.ResumeEdu;
import com.worldelite.job.entity.School;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ResumeEduForm;
import com.worldelite.job.mapper.ResumeEduMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.SchoolMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ResumeEduVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumeEduService extends BaseService{

    @Autowired
    private ResumeEduMapper resumeEduMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    /**
     * 保存简历 - 教育信息
     * @param resumeEduForm
     * @return
     */
    public ResumeEduVo saveResumeEdu(ResumeEduForm resumeEduForm){
//        resumeService.checkResumeCreator(resumeEduForm.getResumeId());

        ResumeEdu resumeEdu = null;
        if(resumeEduForm.getId() != null){
            resumeEdu = resumeEduMapper.selectByPrimaryKey(resumeEduForm.getId());
        }

        if(resumeEdu == null){
            resumeEdu = new ResumeEdu();
            resumeEdu.setResumeId(resumeEduForm.getResumeId());
            if(resumeEduForm.getUserId() != null){
                resumeEdu.setUserId(resumeEduForm.getUserId());
            }else{
                resumeEdu.setUserId(curUser().getId());
            }
        }

        if(StringUtils.isNotEmpty(resumeEduForm.getSchoolName())){
            School school = schoolMapper.selectByName(resumeEduForm.getSchoolName());
            if(school != null){
                resumeEdu.setSchoolId(school.getId());
            }
        }

        resumeEdu.setSchoolName(resumeEduForm.getSchoolName());
        resumeEdu.setMajorName(resumeEduForm.getMajorName());
        resumeEdu.setDegreeId(resumeEduForm.getDegreeId());
        resumeEdu.setStartTime(resumeEduForm.getStartTime());
        resumeEdu.setFinishTime(resumeEduForm.getFinishTime());
        resumeEdu.setGpa(resumeEduForm.getGpa());

        if(resumeEdu.getId() == null){
            resumeEduMapper.insertSelective(resumeEdu);
        }else{
            resumeEdu.setUpdateTime(new Date());
            resumeEduMapper.updateByPrimaryKeySelective(resumeEdu);
        }

        return new ResumeEduVo().asVo(resumeEdu);
    }

    /**
     * 删除教育经历
     *
     * @param id
     */
    public void deleteResumeEdu(Integer id){
        ResumeEdu resumeEdu = resumeEduMapper.selectByPrimaryKey(id);
        if(resumeEdu != null){
//            resumeService.checkResumeCreator(resumeEdu.getResumeId());
            resumeEduMapper.deleteByPrimaryKey(resumeEdu.getId());
        }
    }

    /**
     * 获取简历的教育经历
     *
     * @param resumeId
     * @return
     */
    public List<ResumeEduVo> getResumeEduVoList(Long resumeId){
        ResumeEdu options = new ResumeEdu();
        options.setResumeId(resumeId);
        PageHelper.orderBy("degree_id desc");
        List<ResumeEdu> resumeEduList = resumeEduMapper.selectAndList(options);
        return AppUtils.asVoList(resumeEduList, ResumeEduVo.class);
    }

    public List<ResumeEdu> getResumeEduList(Long resumeId){
        ResumeEdu options = new ResumeEdu();
        options.setResumeId(resumeId);
        PageHelper.orderBy("degree_id desc");
        return resumeEduMapper.selectAndList(options);
    }
}
