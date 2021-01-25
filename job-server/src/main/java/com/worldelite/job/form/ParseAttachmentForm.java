package com.worldelite.job.form;

import lombok.Data;

/**
 * 简历快速填写表单
 */
@Data
public class ParseAttachmentForm {
    private String name; //简历文件名
    private Boolean asAttachment; //是否存为附件
}
