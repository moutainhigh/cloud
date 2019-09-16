package com.smart4y.cloud.core.infrastructure.toolkit.json.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.smart4y.cloud.core.infrastructure.toolkit.json.JsonHelper;
import lombok.extern.apachecommons.CommonsLog;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2017/12/1.
 */
@CommonsLog
public enum JacksonHelper implements JsonHelper {

    INSTANCE;

    public ObjectMapper objectMapper;

    JacksonHelper() {
        objectMapper = new ObjectMapper();
        // 设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 对于空的对象转json的时候不抛出错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用序列化日期为timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁用遇到未知属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 视空字符传为null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 低层级配置
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, Boolean.TRUE);
        // 允许属性名称没有引号
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, Boolean.TRUE);
        // 支持解析单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, Boolean.TRUE);
        // 支持解析结束符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, Boolean.TRUE);
        // Date 时间转换
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORM_PATTERN));
    }

    @Override
    public String toJson(Object object) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return jsonString;
    }

    @Override
    public String toPrettyJson(Object object) {
        String jsonString;
        try {
            jsonString = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return jsonString;
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return null == json || "".equals(json) ?
                    clazz.newInstance() : objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> jsonToMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}