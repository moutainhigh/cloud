package com.smart4y.cloud.mapper.enumhandler;

import java.io.Serializable;

/**
 * 枚举接口
 * <p>
 * 1、新建枚举都应继承该接口；
 * 2、若需要枚举转换则字段值必须加字段{@link javax.persistence.Column}注解
 * </p>
 *
 * @author Youtao
 *         Created By Youtao on 2017/2/15.
 */
public interface IEnum<K> extends Serializable {

    K getCode();
}