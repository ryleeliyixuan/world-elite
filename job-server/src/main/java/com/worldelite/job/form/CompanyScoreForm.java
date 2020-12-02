package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 企业评分表单
 */
@Data
public class CompanyScoreForm {
    private Long id; //评分ID
    private Long companyId; //企业ID
    private Integer score; //分数
    private String content; //评分内容
    private Byte anonymous; //是否匿名
    private Byte recommend; //是否优质
}
