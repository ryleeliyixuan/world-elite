package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.CompanyPost;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 禁言视图对象
 */
@Data
public class CompanyForbiddenVo {
    private boolean forbidden; //是否被禁言
}
