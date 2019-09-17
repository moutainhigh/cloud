package com.smart4y.cloud.gateway.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路由限流 数据
 *
 * @author Youtao
 *         Created by youtao on 2019-09-09.
 */
@Data
@NoArgsConstructor
public class RateLimitApiObj implements Serializable {

    private Long policyId;
    private Long limitQuota;
    private String intervalUnit;
    private String policyName;
    private Long apiId;
    private String apiCode;
    private String apiName;
    private String apiCategory;
    private String serviceId;
    private String path;
    private String url;

    public RateLimitApiObj(Long policyId, Long limitQuota, String intervalUnit, String policyName,
                           Long apiId, String apiCode, String apiName, String apiCategory,
                           String serviceId, String path, String url) {
        this.policyId = policyId;
        this.limitQuota = limitQuota;
        this.intervalUnit = intervalUnit;
        this.policyName = policyName;
        this.apiId = apiId;
        this.apiCode = apiCode;
        this.apiName = apiName;
        this.apiCategory = apiCategory;
        this.serviceId = serviceId;
        this.path = path;
        this.url = url;
    }
}