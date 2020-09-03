package com.smart4y.cloud.uaa.infrastructure.feign;

import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.uaa.infrastructure.feign.fallback.BaseUserFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Youtao
 * Created by youtao on 2019/9/18.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER, contextId = "baseUserFeign", fallbackFactory = BaseUserFeignFallbackFactory.class)
public interface BaseUserFeign {

    /**
     * 获取登录账号信息
     */
    @GetMapping("/user/login")
    ResultMessage<UserAccountVO> userLogin(@RequestParam(value = "username") String username);
}