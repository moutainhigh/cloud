package com.smart4y.cloud.core.infrastructure.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring 上下文容器
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Slf4j
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext未注入,请在ApplicationContext.xml中定义SpringContextHolder");
        }
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == SpringContextHolder.applicationContext) {
            SpringContextHolder.applicationContext = applicationContext;
        }
    }

    /**
     * 通过 name 获取 Bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过 class 获取 Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过 name，以及 Clazz 返回指定的 Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}