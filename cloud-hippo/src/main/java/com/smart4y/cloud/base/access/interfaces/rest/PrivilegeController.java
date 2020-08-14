package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.entity.RbacPrivilege;
import com.smart4y.cloud.base.access.domain.service.PrivilegeService;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 权限"})
@RestController
public class PrivilegeController extends BaseAccessController {

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/privileges/page")
    @ApiOperation(value = "权限:分页")
    public ResultMessage<Page<RbacPrivilege>> getPrivilegesPage(RbacPrivilegePageQuery query) {
        Page<RbacPrivilege> result = privilegeService.getPageLike(
                query.getPage(), query.getLimit(), query.getPrivilegeId(),
                query.getPrivilege(), query.getPrivilegeType());
        return ok(result);
    }
}