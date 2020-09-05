package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeAttach;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.util.TimeUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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
    //简历文件路径
    private String docPath;
    //简历内容
    private String attachContent;

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    public ResumeAttachVo asVo(ResumeAttach resumeAttach) {
        setId(resumeAttach.getId());
        setResumeId(resumeAttach.getResumeId());
        setUserId(resumeAttach.getUserId());
        setDocPath(resumeAttach.getDocPath());
        setAttachContent(resumeAttach.getAttachContent());
        return this;
    }
}
