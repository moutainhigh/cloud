package com.smart4y.cloud.core.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuyadu
 */
@Data
public class RateLimitApi extends BaseApi implements Serializable {

    private Long itemId;
    private Long policyId;
    private String policyName;
    private Long limitQuota;
    private String intervalUnit;
    private String url;
}