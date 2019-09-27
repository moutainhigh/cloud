package com.smart4y.cloud.core.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smart4y.cloud.core.domain.model.AuthorityAction;
import lombok.Data;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityMenuDTO extends BaseMenuDTO implements Serializable {

    /**
     * 权限ID
     */
    private Long authorityId;
    /**
     * 权限标识
     */
    private String authority;
    /**
     * 功能权限列表
     */
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