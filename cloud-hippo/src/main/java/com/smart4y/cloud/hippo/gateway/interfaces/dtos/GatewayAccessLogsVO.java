package com.smart4y.cloud.hippo.gateway.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（访问日志）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayAccessLogsVO", description = "网关（访问日志）")
public class GatewayAccessLogsVO implements Serializable {

    /**
     * 访问ID
     */
    @ApiModelProperty(value = "访问ID")
    private Long accessId;

    /**
     * 访问路径
     */
    @ApiModelProperty(value = "访问路径")
    private String path;

    /**
     * 请求IP
     */
    @ApiModelProperty(value = "请求IP")
    private String ip;

    /**
     * 响应状态
     */
    @ApiModelProperty(value = "响应状态")
    private String httpStatus;

    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String method;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    private String requestTime;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间")
    private String responseTime;

    /**
     * 耗时
     */
    @ApiModelProperty(value = "耗时")
    private Long useTime;

    /**
     * 客户端标识
     */
    @ApiModelProperty(value = "客户端标识")
    private String userAgent;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String region;

    /**
     * 服务名
     */
    @ApiModelProperty(value = "服务名")
    private String serviceId;

    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String error;

    /**
     * 请求数据
     */
    @ApiModelProperty(value = "请求数据")
    private String params;

    /**
     * 请求头
     */
    @ApiModelProperty(value = "请求头")
    private String headers;

    /**
     * 认证信息
     */
    @ApiModelProperty(value = "认证信息")
    private String authentication;

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
