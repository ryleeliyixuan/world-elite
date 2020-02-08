package com.worldelite.job.anatation;

import com.worldelite.job.constants.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户登录
 * @author yeguozhong yedaxia.github.com
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireLogin {

    /**
     * 允许用户类型
     */
    UserType[] allow() default {};
}
