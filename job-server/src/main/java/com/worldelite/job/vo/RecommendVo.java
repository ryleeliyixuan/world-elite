package com.worldelite.job.vo;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class RecommendVo<T> {
    private Integer id;
    private Integer position;
    private T object;
    @JSONField(format = DatePattern.NORM_DATETIME_PATTERN)
    private Date createTime;
}
