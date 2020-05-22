package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class StatusForm {
    private Long userId; //用户ID
    private Byte status; //状态
    private String reason; //原因
}
