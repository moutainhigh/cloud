package com.smart4y.cloud.base.interfaces.valueobject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统资源-功能操作
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseActionVO", description = "系统资源（功能操作）")
public class BaseActionVO implements Serializable {

    /**
     * 资源ID
     */
    @ApiModelProperty(value = "资源ID")
    private Long actionId;

    /**
     * 资源编码
     */
    @ApiModelProperty(value = "资源编码")
    private String actionCode;

    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String actionName;

    /**
     * 资源描述
     */
    @ApiModelProperty(value = "资源描述")
    private String actionDesc;

    /**
     * 资源父节点
     */
    @ApiModelProperty(value = "资源父节点")
    private Long menuId;

    /**
     * 优先级 越小越靠前
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
     * 服务名称
     */
    @ApiModelProperty(value = "服务名称")
    private String serviceId;

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