package com.smart4y.cloud.gateway.domain.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;
import javax.persistence.Id;

/**
 * 网关（路由）
 *
 * @author Youtao on 2020/08/18 15:27
 */
@Data
@Accessors(chain = true)
@Table(name = "gateway_route")
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
     * 路由说明
     */
    @Column(name = "route_desc")
    private String routeDesc;

    /**
     * 路由名称
     */
    @Column(name = "route_name")
    private String routeName;

    /**
     * 路由路径
     */
    @Column(name = "route_path")
    private String routePath;

    /**
     * 路由所属服务（负载均衡）
     */
    @Column(name = "route_service_id")
    private String routeServiceId;

    /**
     * 路由地址（URL反向代理）
     */
    @Column(name = "route_url")
    private String routeUrl;

    /**
     * 路由忽略前缀（0不忽略 1忽略）
     */
    @Column(name = "route_strip_prefix")
    private Boolean routeStripPrefix;

    /**
     * 路由重试（0不重试 1重试）
     */
    @Column(name = "route_retryable")
    private Boolean routeRetryable;

    /**
     * 路由状态（10启用 20禁用 30锁定）
     */
    @Column(name = "route_state")
    private String routeState;

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
