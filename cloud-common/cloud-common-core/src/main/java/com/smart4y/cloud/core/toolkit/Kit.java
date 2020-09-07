package com.smart4y.cloud.core.toolkit;

import com.smart4y.cloud.core.toolkit.complier.CompilerHelper;
import com.smart4y.cloud.core.toolkit.complier.CompilerHelperImpl;
import com.smart4y.cloud.core.toolkit.idcard.IdCardHelper;
import com.smart4y.cloud.core.toolkit.idcard.IdCardHelperImpl;
import com.smart4y.cloud.core.toolkit.json.JsonHelper;
import com.smart4y.cloud.core.toolkit.json.core.JacksonHelper;
import com.smart4y.cloud.core.toolkit.random.RandomHelper;
import com.smart4y.cloud.core.toolkit.reflection.ReflectionHelper;
import com.smart4y.cloud.core.toolkit.secret.AesHelper;
import com.smart4y.cloud.core.toolkit.secret.RsaHelper;
import com.smart4y.cloud.core.toolkit.xml.JacksonXmlHelper;
import com.smart4y.cloud.core.toolkit.xml.XmlHelper;

/**
 * 工具类入口
 *
 * @author Youtao
 *         Created by youtao on 2018/7/30.
 */
public enum Kit {
    INSTANCE;

    public static Kit help() {
        return INSTANCE;
    }

    /**
     * 反射
     *
     * @return {@link ReflectionHelper}
     */
    public ReflectionHelper reflection() {
        return ReflectionHelper.INSTANCE;
    }

    /**
     * Jackson
     *
     * @return {@link JacksonHelper}
     */
    public JsonHelper json() {
        return JacksonHelper.INSTANCE;
    }

    /**
     * 随机数
     *
     * @return {@link RandomHelper}
     */
    public RandomHelper random() {
        return RandomHelper.INSTANCE;
    }

    /**
     * ASE
     *
     * @return {@link AesHelper}
     */
    public AesHelper aes() {
        return AesHelper.INSTANCE;
    }

    /**
     * RSA
     *
     * @return {@link RsaHelper}
     */
    public RsaHelper rsa() {
        return RsaHelper.INSTANCE;
    }

    /**
     * 动态编译
     *
     * @return {@link CompilerHelper}
     */
    public CompilerHelper compiler() {
        return CompilerHelperImpl.INSTANCE;
    }

    /**
     * 身份证信息
     *
     * @return {@link IdCardHelper}
     */
    public IdCardHelper idCard() {
        return IdCardHelperImpl.INSTANCE;
    }

    /**
     * XML
     *
     * @return {@link JacksonXmlHelper}
     */
    public XmlHelper xml() {
        return JacksonXmlHelper.INSTANCE;
    }
}