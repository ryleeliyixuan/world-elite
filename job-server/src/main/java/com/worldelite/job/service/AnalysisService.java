package com.worldelite.job.service;

import com.worldelite.job.form.*;
import com.worldelite.job.mapper.ResumeEduMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Nuo Xu
 */
@Service
public class AnalysisService {
    @Autowired
    private UserApplicantMapper userApplicantMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ResumeEduMapper resumeEduMapper;

    public Integer countUsers(){
        return userApplicantMapper.countAll();
    }

    public Integer countResumes(){
        return resumeMapper.countAll();
    }

    public Integer countEmails(){
        //no email found in t_resume
        return 0;
    }

    public Integer countSchools(){
        return resumeEduMapper.countSchool();
    }

    public List<CountForm> countSchoolGroupBy(){
        return resumeEduMapper.countSchoolGroupBy();
    }

    public List<CountForm> countPlaceGroupBy(){
        return resumeMapper.countPlaceGroupBy();
    }

    public List<GraduateTimePairForm>countGraduateTimeGroupBy(){
        return resumeMapper.countGraduateTimeGroupBy();
    }

    public List<CountForm>countMajorGroupBy(){
        return resumeEduMapper.countMajorGroupBy();
    }

    public List<FlagPairForm>countFlagGroupBy(){
        return userApplicantMapper.countFlagGroupBy();
    }

}
