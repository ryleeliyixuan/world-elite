package com.worldelite.job.service;

import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeEduOptions;
import com.worldelite.job.entity.ResumeOptions;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.ResumeEduMapper;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public List<TimePairForm>countGraduateTimeGroupBy(){
        return resumeMapper.countGraduateTimeGroupBy();
    }

    public List<CountForm>countMajorGroupBy(){
        return resumeEduMapper.countMajorGroupBy();
    }

    public List<FlagPairForm>countFlagGroupBy(){
        return userApplicantMapper.countFlagGroupBy();
    }

    public List<TimePairForm>countAllByTime(Date startTime, Date endTime){
        List<TimePairForm>list = new ArrayList<>();
        int days = (int)( (startTime.getTime() - endTime.getTime()) / (1000 * 60 * 60 * 24));
        for (int i=0;i<days;i++){
            ResumeOptions options = new ResumeOptions();
            options.setStartTime(startTime);
            Date afterOneDay = new Date(startTime.getTime()+(1000 * 60 * 60 * 24));
            options.setEndTime(afterOneDay);
            list.add(resumeMapper.countAllByTime(options));
            startTime = afterOneDay;
        }
        return list;
    }

    public List<TimePairForm>countOpResumeByTime(Date startTime, Date endTime){
        List<TimePairForm>list = new ArrayList<>();
        int days = (int)( (startTime.getTime() - endTime.getTime()) / (1000 * 60 * 60 * 24));
        for (int i=0;i<days;i++){
            ResumeOptions options = new ResumeOptions();
            options.setStartTime(startTime);
            Date afterOneDay = new Date(startTime.getTime()+(1000 * 60 * 60 * 24));
            options.setEndTime(afterOneDay);
            list.add(resumeMapper.countOpResumeByTime(options));
            startTime = afterOneDay;
        }
        return list;
    }

    public List<TimePairForm>countUserResumeByTime(Date startTime, Date endTime){
        List<TimePairForm>list = new ArrayList<>();
        int days = (int)( (startTime.getTime() - endTime.getTime()) / (1000 * 60 * 60 * 24));
        for (int i=0;i<days;i++){
            ResumeOptions options = new ResumeOptions();
            options.setStartTime(startTime);
            Date afterOneDay = new Date(startTime.getTime()+(1000 * 60 * 60 * 24));
            options.setEndTime(afterOneDay);
            list.add(resumeMapper.countUserResumeByTime(options));
            startTime = afterOneDay;
        }
        return list;
    }

    public List<TimePairForm>countSchoolByTime(Date startTime, Date endTime){
        List<TimePairForm>list = new ArrayList<>();
        int days = (int)( (startTime.getTime() - endTime.getTime()) / (1000 * 60 * 60 * 24));
        for (int i=0;i<days;i++){
            ResumeEduOptions options = new ResumeEduOptions();
            options.setStartTime(startTime);
            Date afterOneDay = new Date(startTime.getTime()+(1000 * 60 * 60 * 24));
            options.setEndTime(afterOneDay);
            list.add(resumeEduMapper.countSchoolByTime(options));
            startTime = afterOneDay;
        }
        return list;
    }

}
