package com.smart4y.cloud.base.interfaces.valueobject.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统资源-菜单信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseMenuVO", description = "系统资源（菜单信息）")
public class BaseMenuVO implements Serializable {

    /**
     * 菜单Id
     */
    @ApiModelProperty(value = "菜单Id")
    private Long menuId;

    /**
     * 父级菜单
     */
    @ApiModelProperty(value = "父级菜单")
    private Long parentId;

    /**
     * 菜单编码
     */
    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String menuDesc;

    /**
     * 路径前缀
     */
    @ApiModelProperty(value = "路径前缀")
    private String scheme;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String path;

    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题")
    private String icon;

    /**
     * 打开方式（_self-窗口内 _blank-新窗口）
     */
    @ApiModelProperty(value = "打开方式（_self-窗口内 _blank-新窗口）")
    private String target;

    /**
     * 优先级（越小越靠前）
     */
    @ApiModelProperty(value = "优先级（越小越靠前）")
    private Integer priority;

    /**
     * 状态:0-无效 1-有效
     */
    @ApiModelProperty(value = "状态（0-无效 1-有效）")
    private Integer status;

    /**
     * 服务名
     */
    @ApiModelProperty(value = "服务名")
    private String serviceId;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @ApiModelProperty(value = "保留数据（0-否 1-是不允许删除）")
    private Integer isPersist;

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
