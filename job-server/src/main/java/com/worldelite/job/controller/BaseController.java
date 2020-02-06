package com.worldelite.job.controller;

import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class BaseController {

    @Autowired
    private MessageResource messageSource;

    /**
     *
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */
    protected String message(String code, Object... args){
        return messageSource.getMessage(code, args, "");
    }

    /**
     * get HttpServletRequest instance
     *
     * @return
     */
    protected HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 当前登录用户
     *
     * @return
     */
    protected UserVo curUser() {
        return (UserVo) AppUtils.request().getAttribute(AttrKeys.LOGIN_USER);
    }
}
