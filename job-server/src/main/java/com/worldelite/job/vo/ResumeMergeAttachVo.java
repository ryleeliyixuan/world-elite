package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.ResumeMergeAttach;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Nuo Xu
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class ResumeMergeAttachVo extends EditFlag implements VoConvertable<ResumeMergeAttachVo, ResumeMergeAttach>{

    private Long id;

    private Long resumeId;

    private String resumeAttach;

    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    @JSONField(format = "yyyy-MM-dd")
    private Date updateTime; //更新时间

    @Override
    public ResumeMergeAttachVo asVo(ResumeMergeAttach resumeMergeAttach) {

        BeanUtils.copyProperties(resumeMergeAttach, this);
        return this;
    }
}
