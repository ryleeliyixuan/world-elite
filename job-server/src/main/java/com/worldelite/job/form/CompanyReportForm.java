package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业举报表单
 */
@Data
public class CompanyReportForm {
    private Long ownerId; //对象ID
    private Integer optionId; //选项ID
    private String content; //举报理由
}
