package com.worldelite.job.anatation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于计算简历完成度
 *
 * @author yeguozhong yedaxia.github.com
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResumeScore {
    int value() default 1;
}
