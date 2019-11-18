package com.smart4y.cloud.core.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 请求资源扫描配置
 *
 * @author Youtao
 *         Created by youtao on 2019/11/18.
 */
@Data
@ConfigurationProperties(prefix = "opencloud.scan")
public class OpenScanProperties {

    /**
     * 请求资源注册到API列表
     */
    private boolean registerRequestMapping = false;
}