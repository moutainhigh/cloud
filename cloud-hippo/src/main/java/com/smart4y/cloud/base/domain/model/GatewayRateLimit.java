package com.smart4y.cloud.base.domain.model;

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
 * 网关（流量控制策略）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "gateway_rate_limit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayRateLimit extends BaseEntity<GatewayRateLimit> {

    /**
     * 策略ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 策略名称
     */
    @Column(name = "policy_name")
    private String policyName;

    /**
     * 限流规则类型:url,origin,user
     */
    @Column(name = "policy_type")
    private String policyType;

    /**
     * 限流数
     */
    @Column(name = "limit_quota")
    private Long limitQuota;

    /**
     * 单位时间:seconds-秒,minutes-分钟,hours-小时,days-天
     */
    @Column(name = "interval_unit")
    private String intervalUnit;

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
     * 构造器
     */
    public GatewayRateLimit() {
        super();
    }
}
