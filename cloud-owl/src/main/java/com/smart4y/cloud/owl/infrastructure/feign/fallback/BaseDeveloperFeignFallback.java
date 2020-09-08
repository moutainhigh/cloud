package com.smart4y.cloud.owl.infrastructure.feign.fallback;

import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.toolkit.Kit;
import com.smart4y.cloud.core.dto.UserAccountVO;
import com.smart4y.cloud.owl.infrastructure.feign.BaseDeveloperFeign;
import com.smart4y.cloud.owl.interfaces.command.AddDeveloperThirdPartyCommand;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Youtao
 * on 2020/7/23 15:45
 */
@Slf4j
@Component
public class BaseDeveloperFeignFallback implements BaseDeveloperFeign {

    @Setter
    private Throwable throwable;

    @Override
    public ResultMessage<UserAccountVO> developerLogin(String username) {
        log.error("开发者登录[username={}]异常", username, throwable);
        return ResultMessage.fail();
    }

    @Override
    public ResultMessage<Void> addDeveloperThirdParty(AddDeveloperThirdPartyCommand command) {
        log.error("注册第三方系统登录账号[command={}]异常", Kit.help().json().toJson(command), throwable);
        return ResultMessage.fail();
    }
}