package com.smart4y.cloud.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 功能权限
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityAction implements Serializable {

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

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 是否需要安全认证
     */
    private Boolean isAuth = true;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthorityAction)) {
            return false;
        }
        AuthorityAction a = (AuthorityAction) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}