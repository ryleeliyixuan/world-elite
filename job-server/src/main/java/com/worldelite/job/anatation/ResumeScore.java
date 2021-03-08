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

    /**
     * 包含的值<br/>
     * 优先级低于exclude
     *
     * @return
     */
    String[] include() default {};

    /**
     * 不包含的值, 调用字段的toString().<br/>
     * 优先级高于include
     *
     * @return
     */
    String[] exclude() default {};
}
