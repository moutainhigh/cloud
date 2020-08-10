package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.application.RbacUserApplicationService;
import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.core.exception.OpenAlertException;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/8/7 14:26
 */
@Slf4j
@RestController
@Api(tags = {"访问控制 - 账户"})
public class CurrentController extends BaseAccessController {

    @Autowired
    private RbacUserApplicationService rbacUserApplicationService;

    @GetMapping("/current/menus")
    @ApiOperation(value = "用户:菜单")
    public ResultMessage<List<RbacMenu>> currentUserMenus() {
        long userId = OpenHelper.getUserIdOptional()
                .orElseThrow(() -> new OpenAlertException(MessageType.UNAUTHORIZED));
        List<RbacMenu> result = rbacUserApplicationService.getAllMenus(userId);
        return ok(result);
    }
}