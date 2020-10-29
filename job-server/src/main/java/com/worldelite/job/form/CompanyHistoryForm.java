package com.worldelite.job.form;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * 企业发展路径表单
 */
@Data
public class CompanyHistoryForm {
    private Integer id; //发展路径ID
    private Long companyId; //公司ID
    @JSONField(format = "yyyy-MM-dd")
    private Date eventTime; //标志事件时间
    private String event; //标志事件描述
}
