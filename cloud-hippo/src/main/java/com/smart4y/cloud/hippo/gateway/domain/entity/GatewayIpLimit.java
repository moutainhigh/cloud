package com.smart4y.cloud.hippo.gateway.domain.entity;

import com.smart4y.cloud.core.BaseEntity;
import com.smart4y.cloud.mapper.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 网关（IP访问控制策略）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "gateway_ip_limit")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class GatewayIpLimit extends BaseEntity<GatewayIpLimit> {

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
     * 策略类型:0-拒绝/黑名单 1-允许/白名单
     */
    @Column(name = "policy_type")
    private Integer policyType;

    /**
     * ip地址/IP段:多个用隔开;最多10个
     */
    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 最近一次修改时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * 构造器
     */
    public GatewayIpLimit() {
        super();
    }
}
