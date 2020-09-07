package com.smart4y.cloud.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 远程操作权限
 *
 * @author Youtao on 2020/8/10 11:01
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "RemotePrivilegeOperationDTO", description = "远程操作权限")
public class RemotePrivilegeOperationDTO implements Serializable {

    /**
     * 操作编码
     */
    @ApiModelProperty(value = "操作编码")
    private String operationCode;

    /**
     * 操作拦截的URL前缀
     */
    @ApiModelProperty(value = "操作拦截的URL前缀")
    private String operationPath;

    /**
     * 操作是否需要认证（0不需要 1需要）
     */
    @ApiModelProperty(value = "操作是否需要认证（0不需要 1需要）")
    private Boolean operationAuth;

    /**
     * 操作是否对外开放（0不开放 1开放）
     */
    @ApiModelProperty(value = "操作是否对外开放（0不开放 1开放）")
    private Boolean operationOpen;

    /**
     * 操作状态（10-启用，20-禁用，30-锁定）
     */
    @ApiModelProperty(value = "操作状态（10-启用，20-禁用，30-锁定）")
    private String operationState;

    /**
     * 操作所属服务ID
     */
    @ApiModelProperty(value = "操作所属服务ID")
    private String operationServiceId;
}