package com.smart4y.cloud.gateway.infrastructure.feign;

import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.dto.RemotePrivilegeOperationDTO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.gateway.infrastructure.feign.fallback.RemotePrivilegeFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 远程权限服务
 *
 * @author Youtao on 2020/8/10 10:57
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER, contextId = "remotePrivilegeFeign", fallbackFactory = RemotePrivilegeFeignFallbackFactory.class)
public interface RemotePrivilegeFeign {

    /**
     * 获取所有操作权限列表
     */
    @GetMapping("/access/privileges/operations")
    ResultMessage<List<RemotePrivilegeOperationDTO>> remoteAllOperations();
}