package com.smart4y.cloud.hippo.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（流量控制API接口映射）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayRateLimitApiVO", description = "网关（流量控制API接口映射）")
public class GatewayRateLimitApiVO implements Serializable {

    /**
     * 策略ID
     */
    @ApiModelProperty(value = "策略ID")
    private Long policyId;

    /**
     * 接口API资源Id
     */
    @ApiModelProperty(value = "接口API资源Id")
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
