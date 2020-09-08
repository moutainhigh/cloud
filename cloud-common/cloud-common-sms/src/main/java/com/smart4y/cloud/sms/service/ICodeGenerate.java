package com.smart4y.cloud.sms.service;

/**
 * 验证码生成
 */
@FunctionalInterface
public interface ICodeGenerate {

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    String generate();
}
