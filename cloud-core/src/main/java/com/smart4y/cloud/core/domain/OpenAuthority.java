package com.smart4y.cloud.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 自定义已授权权限标识
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "OpenAuthority", description = "自定义已授权权限标识")
public final class OpenAuthority implements GrantedAuthority {

    /**
     * 权限Id
     */
    @ApiModelProperty(value = "权限Id")
    private String authorityId;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String authority;

    /**
     * 过期时间（用于判断权限是否已过期）
     */
    @ApiModelProperty(value = "过期时间（用于判断权限是否已过期）")
    private Date expireTime;

    /**
     * 权限所有者
     */
    @ApiModelProperty(value = "权限所有者")
    private String owner;

    @JsonProperty("isExpired")
    public Boolean getIsExpired() {
        return this.expireTime != null && System.currentTimeMillis() > this.expireTime.getTime();
    }

    public Date getExpireTime() {
        if (null != this.expireTime) {
            return (Date) expireTime.clone();
        }
        return null;
    }

    public void setExpireTime(Date expireTime) {
        if (null != expireTime) {
            this.expireTime = (Date) expireTime.clone();
        }
    }

    public OpenAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    public OpenAuthority(String authority, Date expireTime) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
        if (null != expireTime) {
            this.expireTime = (Date) expireTime.clone();
        }
    }

    public OpenAuthority(String authorityId, String authority, Date expireTime, String owner) {
        this.authorityId = authorityId;
        this.authority = authority;
        if (null != expireTime) {
            this.expireTime = (Date) expireTime.clone();
        }
        this.owner = owner;
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
}