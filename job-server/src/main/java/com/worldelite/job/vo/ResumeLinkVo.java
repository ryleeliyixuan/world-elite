package com.worldelite.job.vo;

import com.worldelite.job.entity.ResumeLink;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeLinkVo extends EditFlag implements VoConvertable<ResumeLinkVo, ResumeLink>{

    private Integer id;
    private String name;
    private String link;

    @Override
    public ResumeLinkVo asVo(ResumeLink resumeLink) {
        setId(resumeLink.getId());
        setName(resumeLink.getName());
        setLink(resumeLink.getLink());
        return this;
    }
}
