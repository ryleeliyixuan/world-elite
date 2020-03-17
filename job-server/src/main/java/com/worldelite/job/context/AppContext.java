package com.worldelite.job.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Component
public class AppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }

    /**
     * 获取应用上下文对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
