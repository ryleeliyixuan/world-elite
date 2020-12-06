package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 企业帖子表单
 */
@Data
public class CompanyPostForm {
    private Long id; //帖子ID
    private Long companyId; //企业ID
    private Integer cliqueId; //圈子ID
    private String title; //帖子标题
    private String content; //帖子内容
    private String[] tags; //标签列表
    private String image; //帖子图片
    private Byte recommend; //是否设为精品/优质

    public String getTags(){
        return StringUtils.join(tags,',');
    }
}
