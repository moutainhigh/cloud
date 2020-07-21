package com.smart4y.cloud.core.infrastructure.mapper.generator.enums;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Collection;
import java.util.Collections;

/**
 * Entity Lombok 注解
 *
 * @author Youtao
 * on 2020/5/14 14:43
 */
public enum LombokAnnotations {

    DATA("data", "@Data", "lombok.Data"),
    BUILDER("builder", "@Builder", "lombok.Builder"),
    ALL_ARGS_CONSTRUCTOR("allArgsConstructor", "@AllArgsConstructor", "lombok.AllArgsConstructor"),
    NO_ARGS_CONSTRUCTOR("noArgsConstructor", "@NoArgsConstructor", "lombok.NoArgsConstructor"),
    TO_STRING("toString", "@ToString", "lombok.ToString");

    private final String paramName;
    public final String name;
    public final FullyQualifiedJavaType javaType;

    LombokAnnotations(String paramName, String name, String className) {
        this.paramName = paramName;
        this.name = name;
        this.javaType = new FullyQualifiedJavaType(className);
    }

    public static LombokAnnotations getValueOf(String paramName) {
        for (LombokAnnotations annotation : LombokAnnotations.values()) {
            if (String.CASE_INSENSITIVE_ORDER.compare(paramName, annotation.paramName) == 0) {
                return annotation;
            }
        }
        return null;
    }

    public static Collection<LombokAnnotations> getDependencies(LombokAnnotations annotation) {
        if (annotation == ALL_ARGS_CONSTRUCTOR) {
            return Collections.singleton(NO_ARGS_CONSTRUCTOR);
        } else {
            return Collections.emptyList();
        }
    }
}