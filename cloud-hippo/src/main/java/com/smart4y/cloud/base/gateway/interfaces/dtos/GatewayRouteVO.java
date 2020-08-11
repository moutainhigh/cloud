package com.smart4y.cloud.base.gateway.interfaces.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 网关（路由）
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "GatewayRouteVO", description = "网关（路由）")
public class GatewayRouteVO implements Serializable {

    /**
     * 路由ID
     */
    @ApiModelProperty(value = "路由ID")
    private Long routeId;

    /**
     * 路由名称
     */
    @ApiModelProperty(value = "路由名称")
    private String routeName;

    /**
     * 路径
     */
    @ApiModelProperty(value = "路径")
    private String path;

    /**
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    /**
     * 完整地址
     */
    @ApiModelProperty(value = "完整地址")
    private String url;

    /**
     * 忽略前缀（0不忽略 1忽略）
     */
    @ApiModelProperty(value = "忽略前缀（0不忽略 1忽略）")
    private Integer stripPrefix;

    /**
     * 0-不重试 1-重试
     */
    @ApiModelProperty(value = "是否重试（0-不重试 1-重试）")
    private Integer retryable;

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
     * 路由说明
     */
    @ApiModelProperty(value = "路由说明")
    private String routeDesc;

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