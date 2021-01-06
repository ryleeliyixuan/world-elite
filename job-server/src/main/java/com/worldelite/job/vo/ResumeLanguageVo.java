package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class ResumeLanguageVo extends EditFlag implements VoConvertable<ResumeLanguageVo, ResumeLanguage>{

    private Integer id;

    private Long resumeId;

    private String title;

    private String description;

    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间

    @Override
    public ResumeLanguageVo asVo(ResumeLanguage resumeLanguage) {
        BeanUtils.copyProperties(resumeLanguage, this);
        return this;
    }
}
