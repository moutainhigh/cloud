package com.smart4y.cloud.owl.infrastructure.feign;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.owl.interfaces.command.AddDeveloperThirdPartyCommand;
import com.smart4y.cloud.owl.infrastructure.feign.fallback.BaseDeveloperFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Youtao
 * Created by youtao on 2019/9/18.
 */
@FeignClient(value = BaseConstants.BASE_SERVER, contextId = "baseDeveloperFeign", fallbackFactory = BaseDeveloperFeignFallbackFactory.class)
public interface BaseDeveloperFeign {

    /**
     * 获取登录账号信息
     */
    @GetMapping("/developer/login")
    ResultMessage<UserAccountVO> developerLogin(@RequestParam(value = "username") String username);

    /**
     * 注册第三方系统登录账号
     */
    @PostMapping("/developer/register/thirdParty")
    ResultMessage<Void> addDeveloperThirdParty(@RequestBody AddDeveloperThirdPartyCommand command);
}