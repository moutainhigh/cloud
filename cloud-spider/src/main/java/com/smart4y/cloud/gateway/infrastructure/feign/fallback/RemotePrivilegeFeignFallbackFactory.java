package com.smart4y.cloud.gateway.infrastructure.feign.fallback;

import com.smart4y.cloud.gateway.infrastructure.feign.RemotePrivilegeFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author Youtao on 2020/8/10 10:43
 */
@Component
public class RemotePrivilegeFeignFallbackFactory implements FallbackFactory<RemotePrivilegeFeign> {

    @Override
    public RemotePrivilegeFeign create(Throwable throwable) {
        RemotePrivilegeFeignFallback fallback = new RemotePrivilegeFeignFallback();
        fallback.setThrowable(throwable);
        return fallback;
    }
}