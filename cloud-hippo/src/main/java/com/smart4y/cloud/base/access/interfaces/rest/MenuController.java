package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.domain.model.RbacMenu;
import com.smart4y.cloud.core.message.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 菜单"})
@RestController
public class MenuController extends BaseAccessController {

    @GetMapping("/menus")
    @ApiOperation(value = "菜单:所有")
    public ResultMessage<List<RbacMenu>> getMenus() {
        return ok();
    }
}