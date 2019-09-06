package com.smart4y.cloud.core.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smart4y.cloud.core.domain.model.AuthorityAction;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 菜单权限
 *
 * @author liuyadu
 */
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


    private List<AuthorityAction> actionList;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public List<AuthorityAction> getActionList() {
        return actionList;
    }

    public void setActionList(List<AuthorityAction> actionList) {
        this.actionList = actionList;
    }

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