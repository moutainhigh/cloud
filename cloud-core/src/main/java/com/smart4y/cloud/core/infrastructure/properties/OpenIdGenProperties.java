package com.smart4y.cloud.core.infrastructure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义ID生成器配置
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@ConfigurationProperties(prefix = "opencloud.id")
public class OpenIdGenProperties {

    /**
     * 工作ID (0~31)
     */
    private long workId = 0;
    /**
     * 数据中心ID (0~31)
     */
    private long centerId = 0;
}