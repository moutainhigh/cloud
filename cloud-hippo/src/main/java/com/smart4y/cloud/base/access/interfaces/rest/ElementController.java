package com.smart4y.cloud.base.access.interfaces.rest;

import com.smart4y.cloud.base.access.domain.model.RbacElement;
import com.smart4y.cloud.base.access.interfaces.dtos.element.CreateElementCommand;
import com.smart4y.cloud.base.access.interfaces.dtos.element.ModifyElementCommand;
import com.smart4y.cloud.core.message.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.smart4y.cloud.core.message.ResultMessage.ok;

/**
 * @author Youtao
 * on 2020/7/30 15:03
 */
@Slf4j
@Api(tags = {"访问控制 - 页面元素"})
@RestController
public class ElementController extends BaseAccessController {

    @GetMapping("/elements")
    @ApiOperation(value = "元素:所有")
    public ResultMessage<List<RbacElement>> getElements() {
        return ok();
    }

    @PostMapping("/elements")
    @ApiOperation(value = "元素:添加")
    public ResultMessage<Void> createElement(@RequestBody CreateElementCommand command) {
        return ok();
    }

    @PutMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:修改")
    public ResultMessage<Void> modifyElement(@PathVariable("elementId") Long elementId, @RequestBody ModifyElementCommand command) {
        return ok();
    }

    @DeleteMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elementId", value = "元素ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<Void> removeElement(@PathVariable("elementId") Long elementId) {
        return ok();
    }

    @GetMapping("/elements/{elementId}")
    @ApiOperation(value = "元素:详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "elementId", value = "元素ID", required = true, paramType = "path", dataType = "long", example = "122367153805459456")
    })
    public ResultMessage<RbacElement> viewElement(@PathVariable("elementId") Long elementId) {
        return ok();
    }
}