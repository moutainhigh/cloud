package com.smart4y.cloud.core.infrastructure.toolkit.xml;

import java.util.Map;

/**
 * @author Youtao
 *         Created by youtao on 2017/12/1.
 */
@SuppressWarnings("unused")
public interface XmlHelper {

    <T> String toXML(T object);

    <T> String toPrettyXML(T object);

    <T> T xmlToObject(byte[] content, Class<T> clazz);

    /**
     * XML 字符串转对象
     */
    <T> T xmlToObject(String content, Class<T> clazz);

    /**
     * XML 字符串转Map
     */
    <T> Map<String, T> xmlToMap(String content);
}
