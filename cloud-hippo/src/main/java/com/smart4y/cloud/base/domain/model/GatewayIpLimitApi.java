package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 网关（IP访问控制API接口映射）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "gateway_ip_limit_api")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayIpLimitApi extends BaseEntity {

    /**
     * 策略ID
     */
    @Column(name = "policy_id")
    private Long policyId;

    /**
     * 接口资源ID
     */
    @Column(name = "api_id")
    private Long apiId;

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
    public GatewayIpLimitApi() {
        super();
    }
}
