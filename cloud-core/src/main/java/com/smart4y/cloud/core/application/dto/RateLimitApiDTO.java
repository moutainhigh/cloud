package com.smart4y.cloud.core.application.dto;

import com.smart4y.cloud.core.application.dto.BaseApiDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author liuyadu
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RateLimitApiDTO extends BaseApiDTO implements Serializable {

    private Long itemId;
    private Long policyId;
    private String policyName;
    private Long limitQuota;
    private String intervalUnit;
    private String url;
}