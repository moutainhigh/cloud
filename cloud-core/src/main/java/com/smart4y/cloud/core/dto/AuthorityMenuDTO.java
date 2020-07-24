package com.smart4y.cloud.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 菜单权限
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AuthorityMenuDTO", description = "菜单权限")
public class AuthorityMenuDTO implements Serializable {

    /**
     * 菜单Id
     */
    @ApiModelProperty(value = "菜单Id")
    private Long menuId;

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
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 父级菜单
     */
    @ApiModelProperty(value = "父级菜单")
    private Long parentId;

    /**
     * 请求协议:/,http://,https://
     */
    @ApiModelProperty(value = "请求协议（/，http://，https://）")
    private String scheme;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String path;

    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    @ApiModelProperty(value = "打开方式（_self-窗口内 _blank-新窗口）")
    private String target;

    /**
     * 优先级（越小越靠前）
     */
    @ApiModelProperty(value = "优先级（越小越靠前）")
    private Integer priority;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String menuDesc;

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
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
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
     * 功能权限列表
     */
    @ApiModelProperty(value = "功能权限列表")
    private List<AuthorityAction> actionList;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthorityMenuDTO)) {
            return false;
        }
        AuthorityMenuDTO a = (AuthorityMenuDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}