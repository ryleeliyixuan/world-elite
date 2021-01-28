package com.worldelite.job.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InterviewerInfoVo {
    private String id;
    private String nickName;
    private String avatar;
    private String status;
    private DictVo industry;
    private CompanyVo company;
    private String companyLogo;
    private String companyName;
    private String position;
    private List<String> experience;
    private DictVo experienceTime;
    private String description;
    private String introduction;
    private String delFlag;
    private Date createTime;
    private Date updateTime;
    private float avgScore;
    private float hot;
    private float totalInterviewTime;
    private String interviewDirectionType;

    public void setId(Long id){
        this.id = String.valueOf(id);
    }
}
