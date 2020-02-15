package com.worldelite.job.vo;

import com.worldelite.job.util.AppUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ApiResult<Vo> {

    private int code = ApiCode.SUCCESS;
    private String msg;
    private Vo data;

    private final static Map<Integer, String> MESSAGE = new HashMap<>();
    static{
        MESSAGE.put(ApiCode.NEED_LOGIN, "need.login");
        MESSAGE.put(ApiCode.PERMISSION_DENIED, "api.error.permission.denied");
        MESSAGE.put(ApiCode.INVALID_OPERATION, "api.error.invalid.operation");
        MESSAGE.put(ApiCode.INVALID_PARAM, "api.error.invalid.param");
        MESSAGE.put(ApiCode.NOT_ACTIVATE, "api.error.not.activate");
    }

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
        apiResult.msg = StringUtils.isEmpty(errorMsg)? AppUtils.message().getMessage(MESSAGE.get(code)): errorMsg;
        return apiResult;
    }

    public static ApiResult fail(String errorMsg){
        ApiResult apiResult = new ApiResult();
        apiResult.code = ApiCode.FAIL;
        apiResult.msg = errorMsg;
        return apiResult;
    }
}
