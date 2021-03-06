package com.smart4y.cloud.owl.infrastructure.feign.fallback;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.dto.OpenClientDetailsDTO;
import com.smart4y.cloud.owl.infrastructure.feign.BaseAppFeign;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:45
 */
@Slf4j
@Component
public class BaseAppFeignFallback implements BaseAppFeign {

    @Setter
    private Throwable throwable;

    @Override
    public ResultMessage<OpenClientDetailsDTO> getAppClientInfo(String clientId) {
        log.error("查询客户端信息[clientId={}]异常", clientId, throwable);
        return ResultMessage.fail();
    }
}
