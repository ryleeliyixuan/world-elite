package com.worldelite.job.entity;

import lombok.Data;

import java.util.List;

/**
 * 简历详情实体类
 * 包含了简历的所有数据
 */
@Data
public class ResumeDetail {

    //简历ID
    private Long resumeId;

    //所属用户ID
    private Long userId;

    //所属企业ID
    private Long companyId;

    //名字
    private String name;

    //邮箱
    private String email;

    //性别
    private Byte gender;

    //手机
    private Long phone;

    //区号
    private String phoneCode;

    //头像
    private String avatar;

    //简历基础信息
    private Resume resumeBasic;

    //教育信息
    private List<ResumeEdu> resumeEduList;

    //工作经验
    private List<ResumeExperience> resumeExpList;

    //实践经验
    private List<ResumePractice> resumePracticeList;

    //能力/实力标签
    private List<ResumeSkill> resumeSkillList;

    //社交主页
    private List<ResumeLink> resumeLinkList;

    //期望职位
    private List<JobCategory> categoryList;

    //期望城市
    private List<City> cityList;

    //期望薪资
    private Dict salary;
}