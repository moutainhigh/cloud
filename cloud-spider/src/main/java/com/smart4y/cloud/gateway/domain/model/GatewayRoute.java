package com.smart4y.cloud.gateway.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 网关（路由）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "gateway_route")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayRoute extends BaseEntity<GatewayRoute> {

    /**
     * 路由ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "route_id")
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
     * 忽略前缀（0不忽略 1忽略）
     */
    @Column(name = "strip_prefix")
    private Integer stripPrefix;

    /**
     * 0-不重试 1-重试
     */
    @Column(name = "retryable")
    private Integer retryable;

    /**
     * 状态:0-无效 1-有效
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 是否为保留数据:0-否 1-是
     */
    @Column(name = "is_persist")
    private Integer isPersist;

    /**
     * 路由说明
     */
    @Column(name = "route_desc")
    private String routeDesc;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 修改时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * 构造器
     */
    public GatewayRoute() {
        super();
    }
}
