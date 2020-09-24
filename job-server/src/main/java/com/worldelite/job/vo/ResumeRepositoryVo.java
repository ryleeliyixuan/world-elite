package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeRepository;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeRepositoryVo implements VoConvertable<ResumeRepositoryVo, ResumeRepository>{

    private Long id; //简历库简历ID
    private Long companyId; //企业ID
    private Long parserId; //ResumeSDK解析结果ID
    private String name; //姓名
    private String birth; //出生日期
    private String email; //邮箱
    private String phone; //电话
    private String attachResume; //简历路径
    private Date createTime; //记录时间
    private Date updateTime; //更新时间
    private Byte delFlag; //删除标识
    private String introduction; //自我介绍

    @Override
    public ResumeRepositoryVo asVo(ResumeRepository resumeRepository) {
        //setId(resumeRepository.getId());
        setCompanyId(resumeRepository.getCompanyId());
//        setParserId(resumeRepository.getParserId());
//        setName(resumeRepository.getName());
//        setBirth(resumeRepository.getBirth());
//        setEmail(resumeRepository.getEmail());
//        setParserId(resumeRepository.getParserId());
//        setAttachResume(resumeRepository.getAttachResume());
//        setCreateTime(resumeRepository.getCreateTime());
//        setUpdateTime(resumeRepository.getUpdateTime());
//        setIntroduction(resumeRepository.getIntroduction());
//        setDelFlag(resumeRepository.getDelFlag());
        return this;
    }
}
