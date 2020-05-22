package com.worldelite.job.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class DictForm {
    private Integer id; //字典ID
    @NotBlank
    private String name; //字典名称
    @NotNull
    private Integer type; //字典类型
    private String value; //字典值
}
