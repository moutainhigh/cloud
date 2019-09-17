package com.smart4y.cloud.core.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统资源-菜单信息
 */
@Data
@NoArgsConstructor
public class BaseMenuDTO implements Serializable {

    /**
     * 菜单Id
     */
    private Long menuId;
    /**
     * 菜单编码
     */
    private String menuCode;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 父级菜单
     */
    private Long parentId;
    /**
     * 请求协议:/,http://,https://
     */
    private String scheme;
    /**
     * 请求路径
     */
    private String path;
    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    private String target;
    /**
     * 优先级 越小越靠前
     */
    private Integer priority;
    /**
     * 描述
     */
    private String menuDesc;
    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;
    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;
    /**
     * 服务ID
     */
    private String serviceId;
}