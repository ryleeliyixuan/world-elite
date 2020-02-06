package com.worldelite.job.vo;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApiResult<Vo> {

    private int code = ApiCode.SUCCESS;
    private String msg;
    private Vo data;

    public static  ApiResult ok(){
        return ok("success");
    }

    public static<Vo> ApiResult<Vo> ok(Object data){
        ApiResult apiResult = new ApiResult();
        apiResult.code = ApiCode.SUCCESS;
        apiResult.data = data;
        return apiResult;
    }

    public static ApiResult fail(int code, String errorMsg){
        ApiResult apiResult = new ApiResult();
        apiResult.code = code;
        apiResult.msg = errorMsg;
        return apiResult;
    }

    public static ApiResult fail(String errorMsg){
        ApiResult apiResult = new ApiResult();
        apiResult.code = ApiCode.FAIL;
        apiResult.msg = errorMsg;
        return apiResult;
    }
}
