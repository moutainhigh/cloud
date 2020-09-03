package com.smart4y.cloud.core.toolkit.idcard;

/**
 * 身份证信息
 *
 * @author Youtao
 *         Created by youtao on 2018/8/28.
 */
public interface IdCardHelper {

    /**
     * 获取 身份证信息
     *
     * @param idNumber 身份证号
     * @return 身份证信息
     */
    IdentityCard of(String idNumber);
}