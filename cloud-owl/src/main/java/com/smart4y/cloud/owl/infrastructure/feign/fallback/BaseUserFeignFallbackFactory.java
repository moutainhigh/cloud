package com.smart4y.cloud.owl.infrastructure.feign.fallback;

import com.smart4y.cloud.owl.infrastructure.feign.BaseUserFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:43
 */
@Component
public class BaseUserFeignFallbackFactory implements FallbackFactory<BaseUserFeign> {

    @Override
    public BaseUserFeign create(Throwable throwable) {
        BaseUserFeignFallback fallback = new BaseUserFeignFallback();
        fallback.setThrowable(throwable);
        return fallback;
    }
}