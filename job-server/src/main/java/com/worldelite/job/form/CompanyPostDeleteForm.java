package com.worldelite.job.form;

import lombok.Data;

/**
 * 企业帖子删除表单
 */
@Data
public class CompanyPostDeleteForm {
    private Long postId; //帖子ID
    private Long[] postIds; //帖子ID列表
    private String content; //删除理由
}
