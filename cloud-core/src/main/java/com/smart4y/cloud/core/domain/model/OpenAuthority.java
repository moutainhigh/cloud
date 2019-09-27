package com.smart4y.cloud.core.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 自定义已授权权限标识
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OpenAuthority implements GrantedAuthority {

    /**
     * 权限Id
     */
    private String authorityId;
    /**
     * 权限标识
     */
    private String authority;
    /**
     * 过期时间,用于判断权限是否已过期
     */
    private Date expireTime;

    /**
     * 权限所有者
     */
    private String owner;

    public OpenAuthority() {
    }

    public OpenAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    public OpenAuthority(String authority, Date expireTime) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
        this.expireTime = expireTime;
    }

    public OpenAuthority(String authorityId, String authority, Date expireTime, String owner) {
        this.authorityId = authorityId;
        this.authority = authority;
        this.expireTime = expireTime;
        this.owner = owner;
    }

    @JsonProperty("isExpired")
    public Boolean getIsExpired() {
        if (expireTime != null && System.currentTimeMillis() > expireTime.getTime()) {
            return true;
        }
        return false;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof OpenAuthority && this.authority.equals(((OpenAuthority) obj).authority);
        }
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public String toString() {
        return this.authority;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}