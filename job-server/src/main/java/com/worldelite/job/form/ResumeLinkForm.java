package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeLinkForm {
    private Integer id; //链接ID
    private Long resumeId; //简历ID
    @NotBlank(message = "{edit.resume.link.name.not.blank}")
    private String name; //名称
    @NotBlank(message = "{edit.resume.link.url.not.blank}")
    private String link; //链接Url
}
