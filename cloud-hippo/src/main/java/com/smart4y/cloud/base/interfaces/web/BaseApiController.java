package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseApiService;
import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.base.interfaces.command.api.*;
import com.smart4y.cloud.base.interfaces.converter.BaseApiConverter;
import com.smart4y.cloud.base.interfaces.query.BaseApiQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseApiVO;
import com.smart4y.cloud.core.domain.message.ResultMessage;
import com.smart4y.cloud.core.domain.page.Page;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
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
public class BaseApiController {

    @Autowired
    private BaseApiConverter baseApiConverter;
    @Autowired
    private BaseApiService apiService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping(value = "/api")
    public ResultMessage<Page<BaseApiVO>> getApiList(BaseApiQuery query) {
        PageInfo<BaseApi> listPage = apiService.findListPage(query);
        Page<BaseApiVO> result = baseApiConverter.convertPage(listPage);
        return ResultMessage.ok(result);
    }

    /**
     * 获取所有接口列表
     */
    @ApiOperation(value = "获取所有接口列表", notes = "获取所有接口列表")
    @GetMapping("/api/all")
    public ResultMessage<List<BaseApiVO>> getApiAllList(@RequestParam("serviceId") String serviceId) {
        List<BaseApi> allList = apiService.findAllList(serviceId);
        List<BaseApiVO> result = baseApiConverter.convertList(allList);
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
    public ResultMessage<BaseApiVO> getApi(@PathVariable("apiId") Long apiId) {
        BaseApi api = apiService.getApi(apiId);
        BaseApiVO result = baseApiConverter.convert(api);
        return ResultMessage.ok(result);
    }

    /**
     * 添加接口资源
     */
    @ApiOperation(value = "添加接口资源", notes = "添加接口资源")
    @PostMapping("/api/add")
    public ResultMessage<Long> addApi(@RequestBody AddApiCommand command) {
        BaseApi api = new BaseApi();
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
        apiService.addApi(api);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok(apiId);
    }

    /**
     * 编辑接口资源
     */
    @PostMapping("/api/update")
    @ApiOperation(value = "编辑接口资源", notes = "编辑接口资源")
    public ResultMessage<Void> updateApi(@RequestBody UpdateApiCommand command) {
        BaseApi api = new BaseApi();
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
        apiService.updateApi(api);
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
        apiService.removeApi(command.getApiId());
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
        apiService.removeApis(apiIds);
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
        apiService.updateOpenStatusApis(command.getOpen(), apiIds);
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
        apiService.updateStatusApis(command.getStatus(), apiIds);
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
        apiService.updateAuthApis(command.getAuth(), apiIds);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}