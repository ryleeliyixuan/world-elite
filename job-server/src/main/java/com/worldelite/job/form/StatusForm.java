package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class StatusForm {
    private Long userId;
    private Byte status;
    private String reason;
}
