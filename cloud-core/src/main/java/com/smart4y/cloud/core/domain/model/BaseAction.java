package com.smart4y.cloud.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统资源-功能操作
 */
@Data
@NoArgsConstructor
public class BaseAction implements Serializable {

    /**
     * 资源ID
     */
    private Long actionId;

    /**
     * 资源编码
     */
    private String actionCode;

    /**
     * 资源名称
     */
    private String actionName;

    /**
     * 资源父节点
     */
    private Long menuId;

    /**
     * 优先级 越小越靠前
     */
    private Integer priority;

    /**
     * 资源描述
     */
    private String actionDesc;

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