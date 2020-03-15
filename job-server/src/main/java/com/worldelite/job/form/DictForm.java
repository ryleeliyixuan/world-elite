package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class DictForm {
    private Integer id;
    @NotBlank
    private String name;
    @NotNull
    private Integer type;
    private String value;
}
