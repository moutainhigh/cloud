package com.smart4y.cloud.gateway.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（IP访问控制策略）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayIpLimitVO", description = "网关（IP访问控制策略）")
public class GatewayIpLimitVO implements Serializable {

    /**
     * 策略Id
     */
    @ApiModelProperty(value = "策略Id")
    private Long policyId;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String policyName;

    /**
     * 策略类型（0-拒绝/黑名单 1-允许/白名单）
     */
    @ApiModelProperty(value = "策略类型（0-拒绝/黑名单 1-允许/白名单）")
    private Integer policyType;

    /**
     * ip地址/IP段:多个用隔开;最多10个
     */
    @ApiModelProperty(value = "ip地址/IP段（多个用隔开;最多10个）")
    private String ipAddress;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String createdDate;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间（格式：yyyy-MM-dd HH:mm:ss）")
    private String lastModifiedDate;
}
