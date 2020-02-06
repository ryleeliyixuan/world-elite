package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeVo implements VoConvertable<ResumeVo, Resume>{

    private String id;
    private String name;
    private Byte gender;
    private Integer age;
    @JSONField(format = "yyyy-MM-dd")
    private Date birth;
    private ResumeEduVo maxResumeEdu;
    private Byte maritalStatus;
    @JSONField(format = "yyyy-MM")
    private Date returnTime;
    @JSONField(format = "yyyy-MM")
    private Date graduateTime;
    private String curPlace;
    private String phoneCode;
    private String phone;
    private String email;
    private String introduction;

    private List<ResumeEduVo> resumeEduList;

    @Override
    public ResumeVo asVo(Resume resume) {
        setId(String.valueOf(resume.getId()));
        setName(resume.getName());
        setGender(resume.getGender());
        setBirth(resume.getBirth());
        if(resume.getBirth() != null){
            setAge(TimeUtils.getCurrentAge(resume.getBirth()));
        }
        setReturnTime(resume.getReturnTime());
        setGraduateTime(resume.getGraduateTime());
        setCurPlace(resume.getCurPlace());
        setIntroduction(resume.getIntroduction());
        return this;
    }
}
