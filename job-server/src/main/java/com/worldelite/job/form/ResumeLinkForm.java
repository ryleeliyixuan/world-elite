package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ResumeLinkForm {
    private Integer id;
    private Long resumeId;
    @NotBlank(message = "{edit.resume.link.name.not.blank}")
    private String name;
    @NotBlank(message = "{edit.resume.link.url.not.blank}")
    private String link;
}
