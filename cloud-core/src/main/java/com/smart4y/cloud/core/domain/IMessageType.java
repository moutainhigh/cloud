package com.smart4y.cloud.core.domain;

import java.io.Serializable;

/**
 * @author Youtao
 * on 2020/5/27 16:25
 */
public interface IMessageType<K> extends Serializable {

    /**
     * 错误码
     */
    K getRtnCode();

    /**
     * 资源文件中定义的Key
     */
    String getType();
}