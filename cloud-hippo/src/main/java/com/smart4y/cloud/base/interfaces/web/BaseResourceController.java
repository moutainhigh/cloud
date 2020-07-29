package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseResourceService;
import com.smart4y.cloud.base.domain.model.BaseResource;
import com.smart4y.cloud.base.interfaces.command.api.*;
import com.smart4y.cloud.base.interfaces.converter.BaseResourceConverter;
import com.smart4y.cloud.base.interfaces.query.BaseResourceQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseResourceVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = {"系统接口资源管理"})
public class BaseResourceController {

    @Autowired
    private BaseResourceConverter baseResourceConverter;
    @Autowired
    private BaseResourceService baseResourceService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping(value = "/api")
    public ResultMessage<Page<BaseResourceVO>> getApiList(BaseResourceQuery query) {
        PageInfo<BaseResource> listPage = baseResourceService.findListPage(query);
        Page<BaseResourceVO> result = baseResourceConverter.convertPage(listPage);
        return ResultMessage.ok(result);
    }

    /**
     * 获取所有接口列表
     */
    @ApiOperation(value = "获取所有接口列表", notes = "获取所有接口列表")
    @GetMapping("/api/all")
    public ResultMessage<List<BaseResourceVO>> getApiAllList(@RequestParam("serviceId") String serviceId) {
        List<BaseResource> allList = baseResourceService.findAllList(serviceId);
        List<BaseResourceVO> result = baseResourceConverter.convertList(allList);
        return ResultMessage.ok(result);
    }

    /**
     * 获取接口资源
     */
    @GetMapping("/api/{apiId}/info")
    @ApiOperation(value = "获取接口资源", notes = "获取接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "apiId", paramType = "path"),
    })
    public ResultMessage<BaseResourceVO> getApi(@PathVariable("apiId") Long apiId) {
        BaseResource api = baseResourceService.getApi(apiId);
        BaseResourceVO result = baseResourceConverter.convert(api);
        return ResultMessage.ok(result);
    }

    /**
     * 添加接口资源
     */
    @ApiOperation(value = "添加接口资源", notes = "添加接口资源")
    @PostMapping("/api/add")
    public ResultMessage<Long> addApi(@RequestBody AddApiCommand command) {
        BaseResource api = new BaseResource();
        api.setApiCode(command.getApiCode());
        api.setApiName(command.getApiName());
        api.setApiCategory(command.getApiCategory());
        api.setServiceId(command.getServiceId());
        api.setPath(command.getPath());
        api.setStatus(command.getStatus());
        api.setPriority(command.getPriority());
        api.setApiDesc(command.getApiDesc());
        api.setIsAuth(command.getIsAuth());
        api.setIsOpen(command.getIsOpen());
        Long apiId = null;
        baseResourceService.addApi(api);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok(apiId);
    }

    /**
     * 编辑接口资源
     */
    @PostMapping("/api/update")
    @ApiOperation(value = "编辑接口资源", notes = "编辑接口资源")
    public ResultMessage<Void> updateApi(@RequestBody UpdateApiCommand command) {
        BaseResource api = new BaseResource();
        api.setApiId(command.getApiId());
        api.setApiCode(command.getApiCode());
        api.setApiName(command.getApiName());
        api.setApiCategory(command.getApiCategory());
        api.setServiceId(command.getServiceId());
        api.setPath(command.getPath());
        api.setStatus(command.getStatus());
        api.setPriority(command.getPriority());
        api.setApiDesc(command.getApiDesc());
        api.setIsAuth(command.getIsAuth());
        api.setIsOpen(command.getIsOpen());
        baseResourceService.updateApi(api);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除接口资源
     */
    @ApiOperation(value = "移除接口资源", notes = "移除接口资源")
    @PostMapping("/api/remove")
    public ResultMessage<Void> removeApi(@RequestBody DeleteApiCommand command) {
        baseResourceService.removeApi(command.getApiId());
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 批量删除数据
     */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
    })
    @PostMapping("/api/batch/remove")
    public ResultMessage<Void> batchRemove(@RequestBody DeleteApiBatchCommand command) {
        List<Long> apiIds = Arrays.stream(command.getIds().split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        baseResourceService.removeApis(apiIds);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 批量修改公开状态
     */
    @ApiOperation(value = "批量修改公开状态", notes = "批量修改公开状态")
    @PostMapping("/api/batch/update/open")
    public ResultMessage<Void> batchUpdateOpen(@RequestBody UpdateApiBatchOpenCommand command) {
        List<Long> apiIds = Arrays.stream(command.getIds().split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        baseResourceService.updateOpenStatusApis(command.getOpen(), apiIds);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 批量修改状态
     */
    @ApiOperation(value = "批量修改状态", notes = "批量修改状态")
    @PostMapping("/api/batch/update/status")
    public ResultMessage<Void> batchUpdateStatus(@RequestBody UpdateApiBatchStatusCommand command) {
        List<Long> apiIds = Arrays.stream(command.getIds().split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        baseResourceService.updateStatusApis(command.getStatus(), apiIds);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 批量修改身份认证
     */
    @ApiOperation(value = "批量修改身份认证", notes = "批量修改身份认证")
    @PostMapping("/api/batch/update/auth")
    public ResultMessage<Void> batchUpdateAuth(@RequestBody UpdateApiBatchAuthCommand command) {
        List<Long> apiIds = Arrays.stream(command.getIds().split(","))
                .map(Long::parseLong).collect(Collectors.toList());
        baseResourceService.updateAuthApis(command.getAuth(), apiIds);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}