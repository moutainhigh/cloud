package com.smart4y.cloud.uaa.infrastructure.feign;

import com.smart4y.cloud.core.interfaces.UserAccountVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.infrastructure.constants.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/18.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface BaseUserFeign {

    /**
     * 系统用户登录
     */
    @PostMapping("/user/login")
    ResultMessage<UserAccountVO> userLogin(@RequestParam(value = "username") String username);
}