package com.smart4y.cloud.uaa.infrastructure.feign.fallback;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.uaa.infrastructure.feign.BaseUserFeign;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:45
 */
@Slf4j
@Component
public class BaseUserFeignFallback implements BaseUserFeign {

    @Setter
    private Throwable throwable;

    @Override
    public ResultMessage<UserAccountVO> userLogin(String username) {
        log.error("获取登录账号信息[username={}]异常", username, throwable);
        return ResultMessage.fail();
    }
}