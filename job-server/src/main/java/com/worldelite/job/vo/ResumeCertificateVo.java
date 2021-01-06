package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeCertificate;
import com.worldelite.job.entity.ResumeLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class ResumeCertificateVo extends EditFlag implements VoConvertable<ResumeCertificateVo, ResumeCertificate>{

    private Integer id;

    private Long resumeId;

    private String title;

    @JSONField(format = "yyyy-MM-dd")
    private Date time;

    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间


    @Override
    public ResumeCertificateVo asVo(ResumeCertificate resumeCertificate) {
        BeanUtils.copyProperties(resumeCertificate, this);
        return this;
    }
}
