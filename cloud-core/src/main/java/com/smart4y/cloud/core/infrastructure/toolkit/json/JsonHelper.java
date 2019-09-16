package com.smart4y.cloud.core.infrastructure.toolkit.json;

import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 序列化与反序列化 工具类
 *
 * @author Youtao
 *         Created by youtao on 2017/12/1.
 */
public interface JsonHelper {

    String DATE_TIME_FORM_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORM_PATTERN = "yyyy-MM-dd";
    String TIME_FORM_PATTERN = "HH:mm:ss";

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORM_PATTERN);
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORM_PATTERN);
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORM_PATTERN);

    String toJson(Object object);

    String toPrettyJson(Object object);

    <T> T fromJson(String json, Class<T> clazz);

    <T> Map<String, T> jsonToMap(String json);
}