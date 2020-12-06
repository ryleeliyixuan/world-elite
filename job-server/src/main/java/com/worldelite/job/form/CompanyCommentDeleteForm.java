package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业回复删除表单
 */
@Data
public class CompanyCommentDeleteForm {
    private Long commentId; //回复ID
    private Long[] commentIds; //批量删除ID列表
    private String content; //删除理由
}
