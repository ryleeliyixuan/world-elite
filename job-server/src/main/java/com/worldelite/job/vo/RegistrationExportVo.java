package com.worldelite.job.vo;

import lombok.Data;

/**
 * 活动报名导出视图
 */
@Data
public class RegistrationExportVo {
    private String registrationExcel; //报名excel文件下载key
    private String registrationPdf; //报名pdf文件下载key
    private String resumePdf; //简历pdf文件下载key
}
