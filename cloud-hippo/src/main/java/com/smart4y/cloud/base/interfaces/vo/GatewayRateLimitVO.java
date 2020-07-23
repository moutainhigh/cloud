package com.smart4y.cloud.base.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（流量控制策略）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayRateLimitVO", description = "网关（流量控制策略）")
public class GatewayRateLimitVO implements Serializable {

    /**
     * 策略ID
     */
    @ApiModelProperty(value = "策略ID")
    private Long policyId;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称")
    private String policyName;

    /**
     * 限流规则类型:url,origin,user
     */
    @ApiModelProperty(value = "限流规则类型（url,origin,user）")
    private String policyType;

    /**
     * 限流数
     */
    @ApiModelProperty(value = "限流数")
    private Long limitQuota;

    /**
     * 单位时间:seconds-秒,minutes-分钟,hours-小时,days-天
     */
    @ApiModelProperty(value = "单位时间（seconds-秒,minutes-分钟,hours-小时,days-天）")
    private String intervalUnit;

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