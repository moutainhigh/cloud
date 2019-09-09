package com.smart4y.cloud.gateway.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 网关（路由）
 *
 * @author Youtao
 *         Created by youtao on 2019-09-09.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "gateway_route")
public class GatewayRouteJpa implements Serializable {

    /**
     * 路由ID
     */
    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;
    /**
     * 路由名称
     */
    @Column(name = "route_name")
    private String routeName;
    /**
     * 路径
     */
    @Column(name = "path")
    private String path;
    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String serviceId;
    /**
     * 完整地址
     */
    @Column(name = "url")
    private String url;
    /**
     * 忽略前缀
     */
    @Column(name = "strip_prefix", length = 3)
    private Integer stripPrefix;
    /**
     * 0-不重试 1-重试
     */
    @Column(name = "retryable", length = 3)
    private Integer retryable;
    /**
     * 状态:0-无效 1-有效
     */
    @Column(name = "status", length = 3)
    private Integer status;
    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist", length = 3)
    private Integer isPersist;
    /**
     * 路由说明
     */
    @Column(name = "route_desc")
    private String routeDesc;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    public Date createTime;
    /**
     * 修改时间
     */
    @Column(name = "update_time")
    public Date updateTime;
}