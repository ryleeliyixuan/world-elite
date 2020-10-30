package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 企业点赞表单
 */
@Data
public class CompanyLikeForm {
    private Integer id; //点赞ID
    private Long ownerId; //对象ID
}
