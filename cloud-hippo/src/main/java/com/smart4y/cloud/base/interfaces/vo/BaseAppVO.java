package com.smart4y.cloud.base.interfaces.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统应用-基础信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "BaseAppVO", description = "系统应用（基础信息）")
public class BaseAppVO implements Serializable {

    /**
     * 客户端ID
     */
    @ApiModelProperty(value = "客户端ID")
    private String appId;

    /**
     * API访问key
     */
    @ApiModelProperty(value = "API访问key")
    private String apiKey;

    /**
     * API访问密钥
     */
    @ApiModelProperty(value = "API访问密钥")
    private String secretKey;

    /**
     * app名称
     */
    @ApiModelProperty(value = "app名称")
    private String appName;

    /**
     * app英文名称
     */
    @ApiModelProperty(value = "app英文名称")
    private String appNameEn;

    /**
     * 应用图标
     */
    @ApiModelProperty(value = "应用图标")
    private String appIcon;

    /**
     * app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    @ApiModelProperty(value = "app类型（server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用）")
    private String appType;

    /**
     * app描述
     */
    @ApiModelProperty(value = "app描述")
    private String appDesc;

    /**
     * 移动应用操作系统:ios-苹果 android-安卓
     */
    @ApiModelProperty(value = "移动应用操作系统（ios-苹果 android-安卓）")
    private String appOs;

    /**
     * 官网地址
     */
    @ApiModelProperty(value = "官网地址")
    private String website;

    /**
     * 开发者ID:默认为0
     */
    @ApiModelProperty(value = "开发者ID（默认为0）")
    private Long developerId;

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