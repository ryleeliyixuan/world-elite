package com.worldelite.job.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    // 工作类型
    private String workType;

    // 期望工作类型
    private String expectWorkType;

    private Byte priority;

    public void setResumeMergeAttachList(List<ResumeMergeAttach> resumeMergeAttachList) {
        this.resumeMergeAttachList = resumeMergeAttachList;
    }

    public List<ResumeMergeAttach> getResumeMergeAttachList() {
        return resumeMergeAttachList;
    }

    private List<ResumeMergeAttach> resumeMergeAttachList;


    public List<ResumeLanguage> getResumeLanguageList() {
        return resumeLanguageList;
    }

    public void setResumeLanguageList(List<ResumeLanguage> resumeLanguageList) {
        this.resumeLanguageList = resumeLanguageList;
    }

    private List<ResumeLanguage> resumeLanguageList;

    public List<ResumeCertificate> getResumeCertificateList() {
        return resumeCertificateList;
    }

    public void setResumeCertificateList(List<ResumeCertificate> resumeCertificateList) {
        this.resumeCertificateList = resumeCertificateList;
    }

    private List<ResumeCertificate>resumeCertificateList;

    /**
     * 计算简历完成度评分,没有最高分 越高越好
     *
     * @return 分数
     */
    public int calcCompletion() {

        if (StringUtils.isAnyBlank(resumeId.toString(), userId.toString(), name, email, phone.toString())) {
            return 0;
        }
        if (resumeBasic == null) return 0;
        if (StringUtils.isAnyBlank(resumeBasic.getName())) return 0;
        AtomicInteger score = new AtomicInteger();

        try {
            for (Field f : resumeBasic.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.get(resumeBasic) != null && StringUtils.isNotBlank(f.get(resumeBasic).toString())) {
                    score.getAndIncrement();
                }
            }
        } catch (Exception ignored) {
        }

        if (resumeEduList != null && resumeEduList.size() > 0) {
            resumeEduList.forEach(resumeEdu -> {
                for (Field f : resumeEdu.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(resumeEdu) != null && StringUtils.isNotBlank(f.get(resumeEdu).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }

        if (resumeExpList != null && resumeExpList.size() > 0) {
            resumeExpList.forEach(experience -> {
                for (Field f : experience.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(experience) != null && StringUtils.isNotBlank(f.get(experience).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }

        if (resumePracticeList != null && resumePracticeList.size() > 0) {
            resumePracticeList.forEach(resumePractice -> {
                for (Field f : resumePractice.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(resumePractice) != null && StringUtils.isNotBlank(f.get(resumePractice).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }

        if (resumeSkillList != null && resumeSkillList.size() > 0) {
            resumeSkillList.forEach(resumeSkill -> {
                for (Field f : resumeSkill.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(resumeSkill) != null && StringUtils.isNotBlank(f.get(resumeSkill).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }
//        if (resumeLinkList != null && resumeLinkList.size() > 0) {
//            resumeLinkList.forEach(resumeLink -> {
//                for (Field f : resumeLink.getClass().getDeclaredFields()) {
//                    f.setAccessible(true);
//                    try {
//                        if (f.get(resumeLink) != null && StringUtils.isNotBlank(f.get(resumeLink).toString())) {
//                            score.getAndIncrement();
//                        }
//                    } catch (IllegalAccessException ignored) {
//                    }
//                }
//            });
//        }
        //language
        if (resumeLanguageList != null && resumeLanguageList.size() > 0) {
            resumeLanguageList.forEach(resumeLanguage -> {
                for (Field f : resumeLanguage.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(resumeLanguage) != null && StringUtils.isNotBlank(f.get(resumeLanguage).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }
        
        //certificate
        if (resumeCertificateList != null && resumeCertificateList.size() > 0) {
            resumeCertificateList.forEach(resumeCertificate -> {
                for (Field f : resumeCertificate.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(resumeCertificate) != null && StringUtils.isNotBlank(f.get(resumeCertificate).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }
        
        if (categoryList != null && categoryList.size() > 0) {
            categoryList.forEach(jobCategory -> {
                for (Field f : jobCategory.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(jobCategory) != null && StringUtils.isNotBlank(f.get(jobCategory).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }
        if (cityList != null && cityList.size() > 0) {
            cityList.forEach(city -> {
                for (Field f : city.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        if (f.get(city) != null && StringUtils.isNotBlank(f.get(city).toString())) {
                            score.getAndIncrement();
                        }
                    } catch (IllegalAccessException ignored) {
                    }
                }
            });
        }

        if (salary != null) {
            score.getAndIncrement();
        }


        return score.get();
    }
}