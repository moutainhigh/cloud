package com.smart4y.cloud.base.gateway.domain.entity;

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
 * 网关（访问日志）
 *
 * @author Youtao on 2020/08/12 09:36
 */
@Data
@Accessors(chain = true)
@Table(name = "gateway_log")
@EqualsAndHashCode(callSuper = true)
public class GatewayLog extends BaseEntity<GatewayLog> {

    /**
     * 日志ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "log_id")
    private Long logId;

    /**
     * 日志所属服务
     */
    @Column(name = "log_service_id")
    private String logServiceId;

    /**
     * 日志访问路径
     */
    @Column(name = "log_path")
    private String logPath;

    /**
     * 日志请求方法
     */
    @Column(name = "log_method")
    private String logMethod;

    /**
     * 日志客户端标识
     */
    @Column(name = "log_user_agent")
    private String logUserAgent;

    /**
     * 日志请求IP
     */
    @Column(name = "log_ip")
    private String logIp;

    /**
     * 日志响应状态
     */
    @Column(name = "log_http_status")
    private String logHttpStatus;

    /**
     * 日志访问时间
     */
    @Column(name = "log_request_time")
    private LocalDateTime logRequestTime;

    /**
     * 日志响应时间
     */
    @Column(name = "log_response_time")
    private LocalDateTime logResponseTime;

    /**
     * 日志耗时（毫秒）
     */
    @Column(name = "log_use_millis")
    private Long logUseMillis;

    /**
     * 日志区域
     */
    @Column(name = "log_region")
    private String logRegion;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 日志请求数据
     */
    @Column(name = "log_params")
    private String logParams;

    /**
     * 日志请求头
     */
    @Column(name = "log_headers")
    private String logHeaders;

    /**
     * 日志认证信息
     */
    @Column(name = "log_authentication")
    private String logAuthentication;

    /**
     * 日志错误信息
     */
    @Column(name = "log_error")
    private String logError;


    /**
     * 构造器
     */
    public GatewayLog() {
        super();
    }
}
