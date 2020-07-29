package com.smart4y.cloud.base.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统资源-API接口
 *
 * @author Youtao
 * Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseResourceVO", description = "系统资源（API接口）")
public class BaseResourceVO implements Serializable {

    /**
     * 接口ID
     */
    @ApiModelProperty(value = "接口ID")
    private Long apiId;

    /**
     * 接口编码
     */
    @ApiModelProperty(value = "接口编码")
    private String apiCode;

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String apiName;

    /**
     * 接口分类:default-默认分类
     */
    @ApiModelProperty(value = "接口分类（default-默认分类）")
    private String apiCategory;

    /**
     * 资源描述
     */
    @ApiModelProperty(value = "资源描述")
    private String apiDesc;

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
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    private String serviceId;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String path;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
     * 是否需要认证: 0-无认证 1-身份认证 默认:1
     */
    @ApiModelProperty(value = "是否需要认证（0-无认证 1-身份认证 默认1）")
    private Integer isAuth;

    /**
     * 是否公开: 0-内部的 1-公开的
     */
    @ApiModelProperty(value = "是否公开（0-内部的 1-公开的）")
    private Integer isOpen;

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
