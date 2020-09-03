package com.smart4y.cloud.uaa.infrastructure.feign.fallback;

import com.smart4y.cloud.uaa.infrastructure.feign.BaseAppFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:43
 */
@Component
public class BaseAppFeignFallbackFactory implements FallbackFactory<BaseAppFeign> {

    @Override
    public BaseAppFeign create(Throwable throwable) {
        BaseAppFeignFallback fallback = new BaseAppFeignFallback();
        fallback.setThrowable(throwable);
        return fallback;
    }
}
