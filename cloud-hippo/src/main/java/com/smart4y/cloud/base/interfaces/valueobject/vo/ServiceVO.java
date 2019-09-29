package com.smart4y.cloud.base.interfaces.valueobject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 服务
 *
 * @author Youtao
 *         Created by youtao on 2019/9/29.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "ServiceVO", description = "服务")
public class ServiceVO implements Serializable {

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String serviceId;

    /**
     * 服务描述
     */
    @ApiModelProperty(value = "服务描述")
    private String serviceDesc;
}