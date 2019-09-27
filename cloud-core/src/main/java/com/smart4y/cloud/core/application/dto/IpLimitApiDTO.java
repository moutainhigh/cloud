package com.smart4y.cloud.core.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class IpLimitApiDTO extends BaseApiDTO implements Serializable {

    private Long itemId;
    private Long policyId;
    private String policyName;
    private Integer policyType;

    @JsonIgnore
    private String ipAddress;

    private Set<String> ipAddressSet;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        if (StringUtils.isNotBlank(ipAddress)) {
            ipAddressSet = new HashSet<>(Arrays.asList(ipAddress.split(";")));
        }
    }

    public Set<String> getIpAddressSet() {
        return ipAddressSet;
    }

    public void setIpAddressSet(Set<String> ipAddressSet) {
        this.ipAddressSet = ipAddressSet;
    }

    public Integer getPolicyType() {
        return policyType;
    }

    public void setPolicyType(Integer policyType) {
        this.policyType = policyType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
}