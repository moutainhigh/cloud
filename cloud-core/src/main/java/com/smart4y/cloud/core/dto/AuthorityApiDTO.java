package com.smart4y.cloud.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * API权限
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AuthorityApiDTO", description = "API权限")
public class AuthorityApiDTO implements Serializable {

    /**
     * 资源ID
     */
    @ApiModelProperty(value = "资源ID")
    private Long apiId;

    /**
     * 资源编码
     */
    @ApiModelProperty(value = "资源编码")
    private String apiCode;

    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String apiName;

    /**
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    /**
     * 接口分类
     */
    @ApiModelProperty(value = "接口分类")
    private String apiCategory;

    /**
     * 资源路径
     */
    @ApiModelProperty(value = "资源路径")
    private String path;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

    /**
     * 资源描述
     */
    @ApiModelProperty(value = "资源描述")
    private String apiDesc;

    /**
     * 状态:0-无效 1-有效
     */
    @ApiModelProperty(value = "状态（0-无效 1-有效）")
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @ApiModelProperty(value = "保留数据（0-否 1-是不允许删除）")
    private Integer isPersist;

    /**
     * 安全认证:0-否 1-是 默认:1
     */
    @ApiModelProperty(value = "安全认证（0-否 1-是 默认1）")
    private Integer isAuth;

    /**
     * 是否公开访问: 0-内部的 1-公开的
     */
    @ApiModelProperty(value = "是否公开（0-内部的 1-公开的）")
    private Integer isOpen;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    /**
     * 响应类型
     */
    @ApiModelProperty(value = "响应类型")
    private String contentType;

    /**
     * 类名
     */
    @ApiModelProperty(value = "类名")
    private String className;

    /**
     * 方法名
     */
    @ApiModelProperty(value = "方法名")
    private String methodName;

    /**
     * 权限ID
     */
    @ApiModelProperty(value = "权限ID")
    private Long authorityId;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String authority;

    /**
     * 前缀
     */
    @ApiModelProperty(value = "前缀")
    private String prefix;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthorityApiDTO)) {
            return false;
        }
        AuthorityApiDTO a = (AuthorityApiDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}