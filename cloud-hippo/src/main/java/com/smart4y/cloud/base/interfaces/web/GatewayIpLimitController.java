package com.smart4y.cloud.base.interfaces.web;

import com.smart4y.cloud.base.application.GatewayIpLimitService;
import com.smart4y.cloud.base.domain.model.GatewayIpLimit;
import com.smart4y.cloud.base.domain.model.GatewayIpLimitApi;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网关IP访问控制
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Api(tags = "网关IP访问控制")
@RestController
public class GatewayIpLimitController {

    @Autowired
    private GatewayIpLimitService gatewayIpLimitService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping("/gateway/limit/ip")
    public ResultEntity<IPage<GatewayIpLimit>> getIpLimitListPage(@RequestParam(required = false) Map map) {
        IPage<GatewayIpLimit> listPage = gatewayIpLimitService.findListPage(new PageParams(map));
        return ResultEntity.ok(listPage);
    }

    /**
     * 查询策略已绑定API列表
     */
    @ApiOperation(value = "查询策略已绑定API列表", notes = "获取分页接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", paramType = "form"),
    })
    @GetMapping("/gateway/limit/ip/api/list")
    public ResultEntity<List<GatewayIpLimitApi>> getIpLimitApiList(
            @RequestParam("policyId") Long policyId) {
        List<GatewayIpLimitApi> list = gatewayIpLimitService.findIpLimitApiList(policyId);
        return ResultEntity.ok(list);
    }

    /**
     * 绑定API
     *
     * @param policyId 策略ID
     * @param apiIds   API接口ID.多个以,隔开.选填
     */
    @ApiOperation(value = "绑定API", notes = "一个API只能绑定一个策略")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "apiIds", value = "API接口ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/gateway/limit/ip/api/add")
    public ResultEntity addIpLimitApis(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "apiIds", required = false) String apiIds) {
        List<Long> collect = Arrays.stream(apiIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        gatewayIpLimitService.addIpLimitApis(policyId, collect);
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }

    /**
     * 获取IP限制
     */
    @ApiOperation(value = "获取IP限制", notes = "获取IP限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "策略ID", paramType = "path"),
    })
    @GetMapping("/gateway/limit/ip/{policyId}/info")
    public ResultEntity<GatewayIpLimit> getIpLimit(@PathVariable("policyId") Long policyId) {
        GatewayIpLimit policy = gatewayIpLimitService.getIpLimitPolicy(policyId);
        return ResultEntity.ok(policy);
    }

    /**
     * 添加IP限制
     *
     * @param policyName 策略名称
     * @param policyType 策略类型:0-拒绝/黑名单 1-允许/白名单
     * @param ipAddress  ip地址/IP段:多个用隔开;最多10个
     */
    @ApiOperation(value = "添加IP限制", notes = "添加IP限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "policyType", required = true, value = "策略类型:0-拒绝/黑名单 1-允许/白名单", allowableValues = "0,1", paramType = "form"),
            @ApiImplicitParam(name = "ipAddress", required = true, value = "ip地址/IP段:多个用隔开;最多10个", paramType = "form")
    })
    @PostMapping("/gateway/limit/ip/add")
    public ResultEntity<Long> addIpLimit(
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "policyType") Integer policyType,
            @RequestParam(value = "ipAddress") String ipAddress
    ) {
        GatewayIpLimit ipLimit = new GatewayIpLimit();
        ipLimit.setPolicyName(policyName);
        ipLimit.setPolicyType(policyType);
        ipLimit.setIpAddress(ipAddress);
        Long policyId = null;
        GatewayIpLimit result = gatewayIpLimitService.addIpLimitPolicy(ipLimit);
        if (result != null) {
            policyId = result.getPolicyId();
        }
        return ResultEntity.ok(policyId);
    }

    /**
     * 编辑IP限制
     *
     * @param policyId   IP限制ID
     * @param policyName 策略名称
     * @param policyType 策略类型:0-拒绝/黑名单 1-允许/白名单
     * @param ipAddress  ip地址/IP段:多个用隔开;最多10个
     */
    @ApiOperation(value = "编辑IP限制", notes = "编辑IP限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "接口Id", paramType = "form"),
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "policyType", required = true, value = "策略类型:0-拒绝/黑名单 1-允许/白名单", allowableValues = "0,1", paramType = "form"),
            @ApiImplicitParam(name = "ipAddress", required = true, value = "ip地址/IP段:多个用隔开;最多10个", paramType = "form")
    })
    @PostMapping("/gateway/limit/ip/update")
    public ResultEntity updateIpLimit(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "policyType") Integer policyType,
            @RequestParam(value = "ipAddress") String ipAddress) {
        GatewayIpLimit ipLimit = new GatewayIpLimit();
        ipLimit.setPolicyId(policyId);
        ipLimit.setPolicyName(policyName);
        ipLimit.setPolicyType(policyType);
        ipLimit.setIpAddress(ipAddress);
        gatewayIpLimitService.updateIpLimitPolicy(ipLimit);
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }

    /**
     * 移除IP限制
     */
    @ApiOperation(value = "移除IP限制", notes = "移除IP限制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "policyId", paramType = "form"),
    })
    @PostMapping("/gateway/limit/ip/remove")
    public ResultEntity removeIpLimit(
            @RequestParam("policyId") Long policyId
    ) {
        gatewayIpLimitService.removeIpLimitPolicy(policyId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }
}