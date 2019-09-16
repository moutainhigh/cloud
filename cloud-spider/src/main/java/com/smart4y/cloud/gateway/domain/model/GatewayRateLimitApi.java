package com.smart4y.cloud.gateway.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 网关（流量控制API接口映射）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/16.
 */
@Data
@Table(name = "gateway_rate_limit_api")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayRateLimitApi extends BaseEntity {

    /**
     * 限制数量
     */
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 时间间隔(秒)
     */
    @Column(name = "api_id")
    private Long apiId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 构造器
     */
    public GatewayRateLimitApi() {
        super();
    }
}
