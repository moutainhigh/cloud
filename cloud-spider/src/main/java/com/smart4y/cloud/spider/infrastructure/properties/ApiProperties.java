package com.smart4y.cloud.spider.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * 网关属性配置类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-08.
 */
@Data
@ConfigurationProperties(prefix = "cloud.api")
public class ApiProperties {

    /**
     * 是否开启签名验证
     */
    private boolean checkSign = true;
    /**
     * 是否开启动态访问控制
     */
    private boolean accessControl = true;

    /**
     * 是否开启接口调试
     */
    private boolean apiDebug = false;

    /**
     * 始终放行
     */
    private Set<String> permitAll;

    /**
     * 无需鉴权的请求
     */
    private Set<String> authorityIgnores;

    /**
     * 签名忽略请求
     */
    private Set<String> signIgnores;
}