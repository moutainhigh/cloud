package com.smart4y.cloud.core.toolkit.complier;

import java.util.Optional;

/**
 * 动态编译
 *
 * @author Youtao
 *         Created by youtao on 2018/8/28.
 */
public interface CompilerHelper {

    /**
     * 动态编译 Java 代码
     *
     * @param javaCode 完整的 .java 文件字符串
     * @return Class
     */
    Optional<Class<?>> findClass(String javaCode);

    /**
     * 动态编译 Java 代码
     *
     * @param className Java 类名（例如：Apple.java 中的 Apple）
     * @param javaCode  完整的 .java 文件字符串
     * @return Class
     */
    Optional<Class<?>> findClass(String className, String javaCode);

    /**
     * 动态编译 Java 代码
     *
     * @param clazz      Class
     * @param methodName 执行的方法名（例如：hello）
     * @param args       参数列表
     * @return the result of dispatching the method represented by this object on {@code obj} with parameters
     */
    Object invoke(Class<?> clazz, String methodName, Object... args);

    /**
     * 动态编译 Java 代码
     *
     * @param javaCode   完整的 .java 文件字符串
     * @param methodName 执行的方法名（例如：hello）
     * @param args       参数列表
     * @return the result of dispatching the method represented by this object on {@code obj} with parameters
     */
    Object invoke(String javaCode, String methodName, Object... args);
}