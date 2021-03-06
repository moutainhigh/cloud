package com.smart4y.cloud.hippo.interfaces.rest;

import com.smart4y.cloud.hippo.application.PrivilegeApplicationService;
import com.smart4y.cloud.hippo.domain.entity.RbacElement;
import com.smart4y.cloud.hippo.domain.service.ElementService;
import com.smart4y.cloud.hippo.interfaces.dtos.element.CreateElementCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.element.ModifyElementCommand;
import com.smart4y.cloud.hippo.interfaces.dtos.element.RbacElementPageQuery;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 页面元素"})
@RestController
public class ElementController extends BaseAccessController {

    private final ElementService elementService;
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public ElementController(ElementService elementService, PrivilegeApplicationService privilegeApplicationService) {
        this.elementService = elementService;
        this.privilegeApplicationService = privilegeApplicationService;
    }

    @GetMapping("/elements/page")
    @ApiOperation(value = "元素:分页")
    public ResultMessage<Page<RbacElement>> getElementsPage(RbacElementPageQuery query) {
        Page<RbacElement> result = elementService.getPageLike(
                query.getPage(), query.getLimit(), query.getElementName(), query.getElementCode());
        return ok(result);
    }

    @PostMapping("/elements")
    @ApiOperation(value = "元素:添加")
    public ResultMessage<Void> createElement(@RequestBody CreateElementCommand command) {
        privilegeApplicationService.createElement(command);
        return ok();
    }

    @PutMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:修改")
    public ResultMessage<Void> modifyElement(@PathVariable("elementId") Long elementId, @RequestBody ModifyElementCommand command) {
        privilegeApplicationService.modifyElement(elementId, command);
        return ok();
    }

    @DeleteMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elementId", value = "元素ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeElement(@PathVariable("elementId") Long elementId) {
        privilegeApplicationService.removeElement(elementId);
        return ok();
    }

    @GetMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elementId", value = "元素ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacElement> viewElement(@PathVariable("elementId") Long elementId) {
        RbacElement result = elementService.getById(elementId);
        return ok(result);
    }
}