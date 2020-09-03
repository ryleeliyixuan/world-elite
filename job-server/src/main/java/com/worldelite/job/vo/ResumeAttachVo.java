package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeAttachVo implements VoConvertable<ResumeAttachVo, ResumeAttach>{

    //自增ID
    private Integer id;
    //简历ID
    private Long resumeId;
    //用户ID
    private Long userId;
    //简历内容
    private String attachContent;

    @Override
    public ResumeAttachVo asVo(ResumeAttach resumeAttach) {
        setId(resumeAttach.getId());
        setResumeId(resumeAttach.getResumeId());
        setUserId(resumeAttach.getUserId());
        System.out.println(resumeAttach.getAttachContent());
        setAttachContent(resumeAttach.getAttachContent());
        return this;
    }
}
