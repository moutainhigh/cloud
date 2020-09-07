package com.smart4y.cloud.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "IpLimitApiDTO", description = "IP限流")
public class IpLimitApiDTO implements Serializable {

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
     * 状态 0-无效 1-有效
     */
    @ApiModelProperty(value = "状态（0-无效 1-有效）")
    private Integer status;

    /**
     * 保留数据 0-否 1-是不允许删除
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
    @ApiModelProperty(value = "是否公开访问（0-内部的 1-公开的）")
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
    @JsonIgnore
    @ApiModelProperty(value = "ip地址/IP段（多个用隔开;最多10个）")
    private String ipAddress;

    /**
     * ip地址/IP段:多个用隔开;最多10个
     */
    @ApiModelProperty(value = "ip地址/IP段（多个用隔开;最多10个）")
    private Set<String> ipAddressSet;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        if (StringHelper.isNotBlank(ipAddress)) {
            ipAddressSet = new HashSet<>(Arrays.asList(ipAddress.split(";")));
        }
    }
}