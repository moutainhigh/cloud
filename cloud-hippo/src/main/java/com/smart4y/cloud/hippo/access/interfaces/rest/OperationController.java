package com.smart4y.cloud.hippo.access.interfaces.rest;

import com.smart4y.cloud.hippo.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.hippo.access.domain.service.OperationService;
import com.smart4y.cloud.hippo.access.interfaces.dtos.operation.ModifyOperationAuthCommand;
import com.smart4y.cloud.hippo.access.interfaces.dtos.operation.ModifyOperationOpenCommand;
import com.smart4y.cloud.hippo.access.interfaces.dtos.operation.ModifyOperationStateCommand;
import com.smart4y.cloud.hippo.access.interfaces.dtos.operation.RbacOperationPageQuery;
import com.smart4y.cloud.hippo.access.domain.entity.RbacOperation;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public OperationController(OperationService operationService, PrivilegeApplicationService privilegeApplicationService) {
        this.operationService = operationService;
        this.privilegeApplicationService = privilegeApplicationService;
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

    @PutMapping("/operations/{operationIds}/state")
    @ApiOperation(value = "操作:状态:修改")
    public ResultMessage<Void> modifyOperationState(@PathVariable("operationIds") List<Long> operationIds, @RequestBody ModifyOperationStateCommand command) {
        privilegeApplicationService.modifyOperationState(operationIds, command);
        return ok();
    }

    @PutMapping("/operations/{operationIds}/open")
    @ApiOperation(value = "操作:公开访问:修改")
    public ResultMessage<Void> modifyOperationOpen(@PathVariable("operationIds") List<Long> operationIds, @RequestBody ModifyOperationOpenCommand command) {
        privilegeApplicationService.modifyOperationOpen(operationIds, command);
        return ok();
    }

    @PutMapping("/operations/{operationIds}/auth")
    @ApiOperation(value = "操作:身份认证:修改")
    public ResultMessage<Void> modifyOperationAuth(@PathVariable("operationIds") List<Long> operationIds, @RequestBody ModifyOperationAuthCommand command) {
        privilegeApplicationService.modifyOperationAuth(operationIds, command);
        return ok();
    }

    @DeleteMapping("/operations/{operationIds}")
    @ApiOperation(value = "操作:移除")
    public ResultMessage<Void> removeOperation(@PathVariable("operationIds") List<Long> operationIds) {
        privilegeApplicationService.removeOperation(operationIds);
        return ok();
    }
}