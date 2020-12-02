package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业评分删除表单
 */
@Data
public class CompanyScoreDeleteForm {
    private Long scoreId; //评分ID
    private Long[] scoreIds; //评分ID了列表
    private String content; //删除理由
}
