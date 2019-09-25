package com.smart4y.cloud.core.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * API权限
 *
 * @author liuyadu
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorityApiDTO extends BaseApiDTO implements Serializable {

    /**
     * 权限ID
     */
    private Long authorityId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 前缀
     */
    private String prefix;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthorityApiDTO)) {
            return false;
        }
        AuthorityApiDTO a = (AuthorityApiDTO) obj;
        return this.authorityId.equals(a.getAuthorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorityId);
    }
}
