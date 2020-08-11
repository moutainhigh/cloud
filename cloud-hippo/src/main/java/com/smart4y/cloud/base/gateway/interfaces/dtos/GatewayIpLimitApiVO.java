package com.smart4y.cloud.base.gateway.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（IP访问控制API接口映射）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayIpLimitApiVO", description = "网关（IP访问控制API接口映射）")
public class GatewayIpLimitApiVO implements Serializable {

    /**
     * 策略Id
     */
    @ApiModelProperty(value = "策略Id")
    private Long policyId;

    /**
     * 接口资源ID
     */
    @ApiModelProperty(value = "接口资源ID")
    private Long apiId;

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
