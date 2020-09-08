package com.smart4y.cloud.owl.infrastructure.feign.fallback;

import com.smart4y.cloud.owl.infrastructure.feign.BaseDeveloperFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:43
 */
@Component
public class BaseDeveloperFeignFallbackFactory implements FallbackFactory<BaseDeveloperFeign> {

    @Override
    public BaseDeveloperFeign create(Throwable throwable) {
        BaseDeveloperFeignFallback fallback = new BaseDeveloperFeignFallback();
        fallback.setThrowable(throwable);
        return fallback;
    }
}