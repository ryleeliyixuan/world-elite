package com.worldelite.job.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 *
 * @author yeguozhong yedaxia.github.com
 */
@Component
public class MessageResource {

    @Autowired
    private MessageSource messageSource;

    /**
     * 获取某条message
     * @param code
     * @return
     */
    public String getMessage(String code) {
        return getMessage(code, new Object[0]);
    }

    /**
     *
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */
    public String getMessage(String code, Object... args){
        return getMessage(code, args, "");
    }

    /**
     *
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    public String getMessage(String code,Object[] args,String defaultMessage){
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}