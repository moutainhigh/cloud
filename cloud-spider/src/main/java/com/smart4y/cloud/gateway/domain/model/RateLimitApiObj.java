package com.smart4y.cloud.gateway.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.math.BigInteger;

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

    public RateLimitApiObj(BigInteger policyId, BigInteger limitQuota, String intervalUnit, String policyName,
                           BigInteger apiId, String apiCode, String apiName, String apiCategory,
                           String serviceId, String path, String url) {
        this.policyId = policyId.longValue();
        this.limitQuota = limitQuota.longValue();
        this.intervalUnit = intervalUnit;
        this.policyName = policyName;
        this.apiId = apiId.longValue();
        this.apiCode = apiCode;
        this.apiName = apiName;
        this.apiCategory = apiCategory;
        this.serviceId = serviceId;
        this.path = path;
        this.url = url;
    }

    public static RateLimitApiObj castEntity(Object[] co) {
        Class[] c2 = new Class[co.length];
        for (int i = 0; i < co.length; i++) {
            if (co[i] != null) {
                c2[i] = co[i].getClass();
            } else {
                c2[i] = String.class;
            }
        }
        try {
            Constructor<RateLimitApiObj> constructor = RateLimitApiObj.class.getConstructor(c2);
            return constructor.newInstance(co);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}