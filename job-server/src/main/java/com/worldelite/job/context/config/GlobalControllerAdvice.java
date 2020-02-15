package com.worldelite.job.context.config;


import com.worldelite.job.context.MessageResource;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.vo.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult handler(Exception e) {
        if(e instanceof ServiceException){
            return ApiResult.fail(((ServiceException)e).getErrorCode(), e.getMessage());
        } else if(e instanceof MethodArgumentNotValidException){
            List<ObjectError> errorList = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            if(CollectionUtils.isNotEmpty(errorList)){
                return ApiResult.fail(errorList.get(0).getDefaultMessage());
            }else{
                return ApiResult.fail(e.getMessage());
            }
        } else{
            log.error("unexpected error", e);
            return ApiResult.fail(e.getMessage());
        }
    }

}
