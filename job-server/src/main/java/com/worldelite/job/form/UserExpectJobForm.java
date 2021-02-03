package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserExpectJobForm {
    private String expectCity; //意向城市
    private Integer categoryId; //意向职位
    private String salaryId; //期望薪资, 逗号分割可以多个
    private Long resumeId;
    private String expectWorkType; // 工作类型
    private String expectPosition; //expect position
    private String industry;
}
