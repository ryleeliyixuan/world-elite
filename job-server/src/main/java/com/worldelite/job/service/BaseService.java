package com.worldelite.job.service;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yeguozhong yedaxia.github.com
 */
public abstract class BaseService {

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
     * 当前登录用户
     *
     * @return
     */
    protected UserVo curUser() {
        return (UserVo) AppUtils.request().getAttribute(AttrKeys.LOGIN_USER);
    }
}
