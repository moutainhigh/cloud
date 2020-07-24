package com.smart4y.cloud.core.toolkit.complier;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态编译
 *
 * @author Youtao
 *         Created by youtao on 2018/8/28.
 */
@Slf4j
public enum CompilerHelperImpl implements CompilerHelper {

    INSTANCE;

    /**
     * className ==> Class
     */
    private static Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    @Override
    public Optional<Class<?>> findClass(String javaCode) {
        return findClass(ClassBuilder.getClassName(javaCode), javaCode);
    }

    @Override
    public Optional<Class<?>> findClass(String className, String javaCode) {
        if (classMap.containsKey(className)) {
            return Optional.of(classMap.get(className));
        }
        Class<?> clazz = ClassBuilder.buildClass(className, javaCode);
        if (null != clazz) {
            classMap.put(className, clazz);
        }
        return Optional.ofNullable(clazz);
    }

    @Override
    public Object invoke(Class<?> clazz, String methodName, Object... args) {
        Method method = getMethod(clazz, methodName);
        return invokeMethod(clazz, methodName, method, args);
    }

    @Override
    public Object invoke(String javaCode, String methodName, Object... args) {
        Class<?> clazz = this.findClass(javaCode)
                .orElseThrow(() -> new IllegalArgumentException("Clazz 不存在"));
        Method method = getMethod(clazz, methodName);
        return invokeMethod(clazz, methodName, method, args);
    }

    private Object invokeMethod(Class<?> clazz, String methodName, Method method, Object[] args) {
        Object result;
        try {
            result = method.invoke(clazz.newInstance(), args);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("执行指定方法异常：" + methodName);
        }
        return result;
    }

    private Method getMethod(Class<?> clazz, String methodName) {
        if (null == clazz) {
            throw new IllegalArgumentException("Clazz 不存在");
        }
        Method[] methods = clazz.getMethods();
        long count = Arrays.stream(methods).filter(x -> methodName.equals(x.getName())).count();
        if (count > 1) {
            throw new IllegalArgumentException("存在多个相同方法名，暂不支持：" + methodName);
        }
        Method method = null;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        if (null == method) {
            throw new IllegalArgumentException("指定方法不存在：" + methodName);
        }
        return method;
    }
}