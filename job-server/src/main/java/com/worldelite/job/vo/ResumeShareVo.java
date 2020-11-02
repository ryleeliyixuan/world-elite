package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeDownload;
import com.worldelite.job.entity.ResumeShare;
import lombok.Data;

import java.util.Date;

/**
 * 简历分享视图对象
 */
@Data
public class ResumeShareVo implements VoConvertable<ResumeShareVo, ResumeShare>{

    private String originId; //源简历ID
    private String resumeId; //分享后简历ID
    private String fromId; //分享公司ID
    private String toId; //目标公司ID
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date createTime; //开始时间

    @Override
    public ResumeShareVo asVo(ResumeShare resumeShare) {
        setOriginId(String.valueOf(resumeShare.getOriginId()));
        setResumeId(String.valueOf(resumeShare.getResumeId()));
        setFromId(String.valueOf(resumeShare.getFromId()));
        setToId(String.valueOf(resumeShare.getToId()));
        return this;
    }
}