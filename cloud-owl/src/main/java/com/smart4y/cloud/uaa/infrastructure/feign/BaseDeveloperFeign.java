package com.smart4y.cloud.uaa.infrastructure.feign;

import com.smart4y.cloud.core.application.dto.UserAccount;
import com.smart4y.cloud.core.domain.ResultBody;
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
public interface BaseDeveloperFeign {

    /**
     * TODO 开发者登录
     */
    @PostMapping("/developer/login")
    ResultBody<UserAccount> developerLogin(@RequestParam(value = "username") String username);

    /**
     * TODO 注册第三方系统登录账号
     */
    @PostMapping("/developer/register/thirdParty")
    ResultBody addDeveloperThirdParty(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "avatar") String avatar);
}