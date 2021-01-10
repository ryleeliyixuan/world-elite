package com.worldelite.job.form;

import lombok.Data;

import javax.print.DocFlavor;
import javax.validation.constraints.NotEmpty;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    private Integer[] cityIds; //意向城市
    private Integer[] categoryIds; //意向职位
    private Integer salaryId;
    private Long userId; //用户ID
    private String expectWorkType; // 工作类型
}
