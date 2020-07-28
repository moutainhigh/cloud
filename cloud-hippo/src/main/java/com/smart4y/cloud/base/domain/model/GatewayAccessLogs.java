package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.mapper.BaseEntity;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 网关（访问日志）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "gateway_access_logs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayAccessLogs extends BaseEntity<GatewayAccessLogs> {

    /**
     * 访问ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "access_id")
    private Long accessId;

    /**
     * 访问路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 请求IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 响应状态
     */
    @Column(name = "http_status")
    private String httpStatus;

    /**
     * 请求方法
     */
    @Column(name = "method")
    private String method;

    /**
     * 访问时间
     */
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    /**
     * 响应时间
     */
    @Column(name = "response_time")
    private LocalDateTime responseTime;

    /**
     * 耗时
     */
    @Column(name = "use_time")
    private Long useTime;

    /**
     * 客户端标识
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 区域
     */
    @Column(name = "region")
    private String region;

    /**
     * 服务名
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 错误信息
     */
    @Column(name = "error")
    private String error;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 最后修改时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * 请求数据
     */
    @Column(name = "params")
    private String params;

    /**
     * 请求头
     */
    @Column(name = "headers")
    private String headers;

    /**
     * 认证信息
     */
    @Column(name = "authentication")
    private String authentication;

    /**
     * 构造器
     */
    public GatewayAccessLogs() {
        super();
    }
}
