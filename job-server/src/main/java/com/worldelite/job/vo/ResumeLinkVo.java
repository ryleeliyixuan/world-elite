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

    private Integer id; //链接ID
    private String name; //名称
    private String link; //链接url

    @Override
    public ResumeLinkVo asVo(ResumeLink resumeLink) {
        setId(resumeLink.getId());
        setName(resumeLink.getName());
        setLink(resumeLink.getLink());
        return this;
    }
}
