package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayIpLimitService;
import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.domain.model.GatewayIpLimitApi;
import com.smart4y.cloud.base.interfaces.command.gateway.iplimit.AddIpLimitApiCommand;
import com.smart4y.cloud.base.interfaces.command.gateway.iplimit.AddIpLimitCommand;
import com.smart4y.cloud.base.interfaces.command.gateway.iplimit.DeleteIpLimitCommand;
import com.smart4y.cloud.base.interfaces.command.gateway.iplimit.UpdateIpLimitCommand;
import com.smart4y.cloud.base.interfaces.converter.GatewayIpLimitApiConverter;
import com.smart4y.cloud.base.interfaces.converter.GatewayIpLimitConverter;
import com.smart4y.cloud.base.interfaces.query.IpLimitQuery;
import com.smart4y.cloud.base.interfaces.vo.GatewayIpLimitApiVO;
import com.smart4y.cloud.base.interfaces.vo.GatewayIpLimitVO;
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
 * 网关IP访问控制
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "网关IP访问控制")
public class GatewayIpLimitController {

    @Autowired
    private GatewayIpLimitConverter gatewayIpLimitConverter;
    @Autowired
    private GatewayIpLimitApiConverter gatewayIpLimitApiConverter;
    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping("/gateway/limit/ip")
    public ResultMessage<Page<GatewayIpLimitVO>> getIpLimitListPage(IpLimitQuery query) {
        PageInfo<GatewayIpLimit> listPage = gatewayIpLimitService.findListPage(query);
        Page<GatewayIpLimitVO> result = gatewayIpLimitConverter.convertPage(listPage);
        return ResultMessage.ok(result);
    }

    /**
     * 获取IP限制
     */
    @ApiOperation(value = "获取IP限制", notes = "获取IP限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "策略ID", paramType = "path"),
    })
    @GetMapping("/gateway/limit/ip/{policyId}/info")
    public ResultMessage<GatewayIpLimitVO> getIpLimit(@PathVariable("policyId") Long policyId) {
        GatewayIpLimit policy = gatewayIpLimitService.getIpLimitPolicy(policyId);
        GatewayIpLimitVO result = gatewayIpLimitConverter.convert(policy);
        return ResultMessage.ok(result);
    }

    /**
     * 查询策略已绑定API列表
     */
    @ApiOperation(value = "查询策略已绑定API列表", notes = "获取分页接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", paramType = "form"),
    })
    @GetMapping("/gateway/limit/ip/api/list")
    public ResultMessage<List<GatewayIpLimitApiVO>> getIpLimitApiList(@RequestParam("policyId") Long policyId) {
        List<GatewayIpLimitApi> list = gatewayIpLimitService.findIpLimitApiList(policyId);
        List<GatewayIpLimitApiVO> result = gatewayIpLimitApiConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 绑定API
     */
    @ApiOperation(value = "绑定API", notes = "一个API只能绑定一个策略")
    @PostMapping("/gateway/limit/ip/api/add")
    public ResultMessage<Void> addIpLimitApis(@RequestBody AddIpLimitApiCommand command) {
        List<Long> collect = Arrays.stream(command.getApiIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        gatewayIpLimitService.addIpLimitApis(command.getPolicyId(), collect);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 添加IP限制
     */
    @ApiOperation(value = "添加IP限制", notes = "添加IP限制")
    @PostMapping("/gateway/limit/ip/add")
    public ResultMessage<Long> addIpLimit(@RequestBody AddIpLimitCommand command) {
        GatewayIpLimit ipLimit = new GatewayIpLimit();
        ipLimit.setPolicyName(command.getPolicyName());
        ipLimit.setPolicyType(command.getPolicyType());
        ipLimit.setIpAddress(command.getIpAddress());
        Long policyId = null;
        GatewayIpLimit result = gatewayIpLimitService.addIpLimitPolicy(ipLimit);
        if (result != null) {
            policyId = result.getPolicyId();
        }
        return ResultMessage.ok(policyId);
    }

    /**
     * 编辑IP限制
     */
    @ApiOperation(value = "编辑IP限制", notes = "编辑IP限制")
    @PostMapping("/gateway/limit/ip/update")
    public ResultMessage<Void> updateIpLimit(@RequestBody UpdateIpLimitCommand command) {
        GatewayIpLimit ipLimit = new GatewayIpLimit();
        ipLimit.setPolicyId(command.getPolicyId());
        ipLimit.setPolicyName(command.getPolicyName());
        ipLimit.setPolicyType(command.getPolicyType());
        ipLimit.setIpAddress(command.getIpAddress());
        gatewayIpLimitService.updateIpLimitPolicy(ipLimit);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除IP限制
     */
    @ApiOperation(value = "移除IP限制", notes = "移除IP限制")
    @PostMapping("/gateway/limit/ip/remove")
    public ResultMessage<Void> removeIpLimit(@RequestBody DeleteIpLimitCommand command) {
        gatewayIpLimitService.removeIpLimitPolicy(command.getPolicyId());
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}