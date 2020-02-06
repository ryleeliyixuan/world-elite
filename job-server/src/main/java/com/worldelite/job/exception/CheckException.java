package com.worldelite.job.exception;

import com.worldelite.job.vo.ApiCode;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class CheckException extends RuntimeException{

    private int errorCode = ApiCode.INVALID_PARAM;

    public CheckException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
