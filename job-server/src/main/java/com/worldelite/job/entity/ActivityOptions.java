package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityOptions extends Activity{
    private String cityIds;
    private String keyword;

    private String organizerName;
    private Integer reviewType;
}
