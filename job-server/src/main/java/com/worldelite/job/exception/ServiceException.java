package com.worldelite.job.exception;

import com.worldelite.job.vo.ApiCode;

/**
 * 业务层错误
 *
 * @author yeguozhong yedaxia.github.com
 */
public class ServiceException extends RuntimeException{

    private int errorCode = ApiCode.FAIL;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
