package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.domain.entity.RbacOperation;
import com.smart4y.cloud.base.access.domain.service.OperationService;
import com.smart4y.cloud.base.access.interfaces.dtos.operation.RbacOperationPageQuery;
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
@Api(tags = {"访问控制 - 功能操作"})
@RestController
public class OperationController extends BaseAccessController {

    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/operations/page")
    @ApiOperation(value = "操作:分页")
    public ResultMessage<Page<RbacOperation>> getOperationsPage(RbacOperationPageQuery query) {
        Page<RbacOperation> result = operationService.getPageLike(
                query.getPage(), query.getLimit(), query.getOperationName(), query.getOperationPath(),
                query.getOperationServiceId(), query.getOperationDesc()
        );
        return ok(result);
    }

    // TODO 删除、修改状态、修改公开访问、修改身份认证
}