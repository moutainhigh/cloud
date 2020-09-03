package com.smart4y.cloud.gateway.infrastructure.feign.fallback;

import com.smart4y.cloud.core.dto.RemotePrivilegeOperationDTO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.gateway.infrastructure.feign.RemotePrivilegeFeign;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Youtao
 * on 2020/7/23 15:45
 */
@Slf4j
@Component
public class RemotePrivilegeFeignFallback implements RemotePrivilegeFeign {

    @Setter
    private Throwable throwable;

    @Override
    public ResultMessage<List<RemotePrivilegeOperationDTO>> remoteAllOperations() {
        log.error("获取远程操作权限异常", throwable);
        return ResultMessage.fail();
    }
}