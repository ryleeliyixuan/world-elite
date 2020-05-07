package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumePractice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumePracticeVo extends EditFlag implements VoConvertable<ResumePracticeVo, ResumePractice>{

    private Integer id;
    @JSONField(format = "yyyy-MM")
    private Date startTime;
    @JSONField(format = "yyyy-MM")
    private Date finishTime;
    private String title;
    private String description;
    private String post;

    @Override
    public ResumePracticeVo asVo(ResumePractice resumePractice) {
        setId(resumePractice.getId());
        setStartTime(resumePractice.getStartTime());
        setFinishTime(resumePractice.getFinishTime());
        setTitle(resumePractice.getTitle());
        setDescription(resumePractice.getDescription());
        setPost(resumePractice.getPost());
        return this;
    }
}
