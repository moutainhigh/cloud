package com.smart4y.cloud.core.toolkit.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2017/12/1.
 */
@Slf4j
public enum JacksonXmlHelper implements XmlHelper {

    INSTANCE;

    private static final String DATE_FORM_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private XmlMapper objectMapper;

    JacksonXmlHelper() {
        objectMapper = new XmlMapper();
        //设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 对于空的对象转json的时候不抛出错误
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用遇到未知属性抛出异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 视空字符传为null
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 允许出现特殊字符和转义符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORM_PATTERN));
    }

    @Override
    public <T> String toXML(T object) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return jsonString;
    }

    @Override
    public <T> String toPrettyXML(T object) {
        String jsonString = null;
        try {
            jsonString = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return jsonString;
    }

    @Override
    public <T> T xmlToObject(byte[] content, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return t;
    }

    @Override
    public <T> T xmlToObject(String content, Class<T> clazz) {
        T t = null;
        try {
            t = objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> xmlToMap(String content) {
        return xmlToObject(content, Map.class);
    }
}
