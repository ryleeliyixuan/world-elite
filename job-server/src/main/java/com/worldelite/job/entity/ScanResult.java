package com.worldelite.job.entity;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ScanResult {

    public static final int CODE_BlOCK = 2;
    public static final int CODE_PASS = 1;
    public static final int CODE_REVIEW = 3;
    public static final int CODE_ERROR = 4;

    private int code;
    private String msg;
}
