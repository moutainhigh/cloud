package com.smart4y.cloud.base.gateway.interfaces.rest;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.gateway.application.GatewayRateLimitService;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayRateLimit;
import com.smart4y.cloud.base.gateway.domain.entity.GatewayRateLimitApi;
import com.smart4y.cloud.base.gateway.interfaces.dtos.AddRateLimitApiCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.AddRateLimitCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.DeleteRateLimitCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.UpdateRateLimitCommand;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRateLimitApiConverter;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRateLimitConverter;
import com.smart4y.cloud.base.gateway.interfaces.dtos.RateLimitQuery;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRateLimitApiVO;
import com.smart4y.cloud.base.gateway.interfaces.dtos.GatewayRateLimitVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关流量控制
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@RestController
@Api(tags = "网关流量控制")
public class GatewayRateLimitController {

    @Autowired
    private GatewayRateLimitConverter gatewayRateLimitConverter;
    @Autowired
    private GatewayRateLimitApiConverter gatewayRateLimitApiConverter;
    @Autowired
    private GatewayRateLimitService gatewayRateLimitService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping("/gateway/limit/rate")
    public ResultMessage<Page<GatewayRateLimitVO>> getRateLimitListPage(RateLimitQuery query) {
        PageInfo<GatewayRateLimit> listPage = gatewayRateLimitService.findListPage(query);
        Page<GatewayRateLimitVO> result = gatewayRateLimitConverter.convertPage(listPage);
        return ResultMessage.ok(result);
    }

    /**
     * 获取流量控制
     */
    @ApiOperation(value = "获取流量控制", notes = "获取流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "策略ID", paramType = "path"),
    })
    @GetMapping("/gateway/limit/rate/{policyId}/info")
    public ResultMessage<GatewayRateLimitVO> getRateLimit(@PathVariable("policyId") Long policyId) {
        GatewayRateLimit policy = gatewayRateLimitService.getRateLimitPolicy(policyId);
        GatewayRateLimitVO result = gatewayRateLimitConverter.convert(policy);
        return ResultMessage.ok(result);
    }

    /**
     * 查询策略已绑定API列表
     */
    @ApiOperation(value = "查询策略已绑定API列表", notes = "获取分页接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", paramType = "form"),
    })
    @GetMapping("/gateway/limit/rate/api/list")
    public ResultMessage<List<GatewayRateLimitApiVO>> getRateLimitApiList(@RequestParam("policyId") Long policyId) {
        List<GatewayRateLimitApi> list = gatewayRateLimitService.findRateLimitApiList(policyId);
        List<GatewayRateLimitApiVO> result = gatewayRateLimitApiConverter.convertList(list);
        return ResultMessage.ok(result);
    }

    /**
     * 绑定API
     */
    @ApiOperation(value = "绑定API", notes = "一个API只能绑定一个策略")
    @PostMapping("/gateway/limit/rate/api/add")
    public ResultMessage<Void> addRateLimitApis(@RequestBody AddRateLimitApiCommand command) {
        gatewayRateLimitService.addRateLimitApis(command.getPolicyId(), StringHelper.isNotBlank(command.getApiIds()) ? command.getApiIds().split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 添加流量控制
     */
    @ApiOperation(value = "添加流量控制", notes = "添加流量控制")
    @PostMapping("/gateway/limit/rate/add")
    public ResultMessage<Long> addRateLimit(@RequestBody AddRateLimitCommand command) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyName(command.getPolicyName());
        rateLimit.setLimitQuota(command.getLimitQuota());
        rateLimit.setIntervalUnit(command.getIntervalUnit());
        rateLimit.setPolicyType(command.getPolicyType());
        Long policyId = null;
        GatewayRateLimit result = gatewayRateLimitService.addRateLimitPolicy(rateLimit);
        if (result != null) {
            policyId = result.getPolicyId();
        }
        return ResultMessage.ok(policyId);
    }

    /**
     * 编辑流量控制
     */
    @ApiOperation(value = "编辑流量控制", notes = "编辑流量控制")
    @PostMapping("/gateway/limit/rate/update")
    public ResultMessage<Void> updateRateLimit(@RequestBody UpdateRateLimitCommand command) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyId(command.getPolicyId());
        rateLimit.setPolicyName(command.getPolicyName());
        rateLimit.setLimitQuota(command.getLimitQuota());
        rateLimit.setIntervalUnit(command.getIntervalUnit());
        rateLimit.setPolicyType(command.getPolicyType());
        gatewayRateLimitService.updateRateLimitPolicy(rateLimit);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 移除流量控制
     */
    @ApiOperation(value = "移除流量控制", notes = "移除流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "policyId", paramType = "form"),
    })
    @PostMapping("/gateway/limit/rate/remove")
    public ResultMessage<Void> removeRateLimit(@RequestBody DeleteRateLimitCommand command) {
        gatewayRateLimitService.removeRateLimitPolicy(command.getPolicyId());
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}