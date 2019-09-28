package com.smart4y.cloud.core.interfaces;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 资源权限
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "AuthorityResourceDTO", description = "资源权限")
public class AuthorityResourceDTO implements Serializable {

    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String path;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String authority;

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID")
    private Long authorityId;

    /**
     * 是否身份认证
     */
    @ApiModelProperty(value = "是否身份认证")
    private Integer isAuth;

    /**
     * 是否公开: 0-内部的 1-公开的
     */
    @ApiModelProperty(value = "是否公开（0-内部的 1-公开的）")
    private Integer isOpen;

    /**
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String serviceId;

    /**
     * 前缀
     */
    @ApiModelProperty(value = "前缀")
    private String prefix;

    /**
     * 资源状态
     */
    @ApiModelProperty(value = "资源状态")
    private Integer status;
}