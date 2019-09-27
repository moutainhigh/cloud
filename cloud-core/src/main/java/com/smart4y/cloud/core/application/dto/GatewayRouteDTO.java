package com.smart4y.cloud.core.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关动态路由
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GatewayRouteDTO implements Serializable {

    /**
     * 路由ID
     */
    private Long routeId;
    /**
     * 路由名称
     */
    private String routeName;
    /**
     * 路径
     */
    private String path;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 完整地址
     */
    private String url;
    /**
     * 忽略前缀
     */
    private Integer stripPrefix;
    /**
     * 0-不重试 1-重试
     */
    private Integer retryable;
    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;
    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;
    /**
     * 路由说明
     */
    private String routeDesc;
}