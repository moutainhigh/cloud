package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.GatewayRateLimitService;
import com.smart4y.cloud.base.domain.model.GatewayRateLimit;
import com.smart4y.cloud.base.domain.model.GatewayRateLimitApi;
import com.smart4y.cloud.base.interfaces.converter.GatewayRateLimitApiConverter;
import com.smart4y.cloud.base.interfaces.converter.GatewayRateLimitConverter;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRateLimitApiVO;
import com.smart4y.cloud.base.interfaces.valueobject.vo.GatewayRateLimitVO;
import com.smart4y.cloud.core.domain.Page;
import com.smart4y.cloud.core.domain.PageParams;
import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 网关流量控制
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
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
    public ResultEntity<Page<GatewayRateLimitVO>> getRateLimitListPage(@RequestParam(required = false) Map map) {
        PageInfo<GatewayRateLimit> listPage = gatewayRateLimitService.findListPage(new PageParams(map));
        Page<GatewayRateLimitVO> result = gatewayRateLimitConverter.convertPage(listPage);
        return ResultEntity.ok(result);
    }

    /**
     * 获取流量控制
     */
    @ApiOperation(value = "获取流量控制", notes = "获取流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "策略ID", paramType = "path"),
    })
    @GetMapping("/gateway/limit/rate/{policyId}/info")
    public ResultEntity<GatewayRateLimitVO> getRateLimit(@PathVariable("policyId") Long policyId) {
        GatewayRateLimit policy = gatewayRateLimitService.getRateLimitPolicy(policyId);
        GatewayRateLimitVO result = gatewayRateLimitConverter.convert(policy);
        return ResultEntity.ok(result);
    }

    /**
     * 查询策略已绑定API列表
     */
    @ApiOperation(value = "查询策略已绑定API列表", notes = "获取分页接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", paramType = "form"),
    })
    @GetMapping("/gateway/limit/rate/api/list")
    public ResultEntity<List<GatewayRateLimitApiVO>> getRateLimitApiList(@RequestParam("policyId") Long policyId) {
        List<GatewayRateLimitApi> list = gatewayRateLimitService.findRateLimitApiList(policyId);
        List<GatewayRateLimitApiVO> result = gatewayRateLimitApiConverter.convertList(list);
        return ResultEntity.ok(result);
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
            @ApiImplicitParam(name = "apiIds", value = "API接口ID.多个以,隔开.选填", paramType = "form")
    })
    @PostMapping("/gateway/limit/rate/api/add")
    public ResultEntity addRateLimitApis(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "apiIds", required = false) String apiIds
    ) {
        gatewayRateLimitService.addRateLimitApis(policyId, StringUtil.isNotBlank(apiIds) ? apiIds.split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }

    /**
     * 添加流量控制
     *
     * @param policyName   策略名称
     * @param limitQuota   限制数
     * @param intervalUnit 单位时间
     * @param policyType   限流规则类型
     */
    @ApiOperation(value = "添加流量控制", notes = "添加流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "policyType", required = true, value = "限流规则类型:url,origin,user", allowableValues = "url,origin,user", paramType = "form"),
            @ApiImplicitParam(name = "limitQuota", required = true, value = "限制数", paramType = "form"),
            @ApiImplicitParam(name = "intervalUnit", required = true, value = "单位时间:seconds-秒,minutes-分钟,hours-小时,days-天", allowableValues = "seconds,minutes,hours,days", paramType = "form"),
    })
    @PostMapping("/gateway/limit/rate/add")
    public ResultEntity<Long> addRateLimit(
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "policyType") String policyType,
            @RequestParam(value = "limitQuota") Long limitQuota,
            @RequestParam(value = "intervalUnit") String intervalUnit) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyName(policyName);
        rateLimit.setLimitQuota(limitQuota);
        rateLimit.setIntervalUnit(intervalUnit);
        rateLimit.setPolicyType(policyType);
        Long policyId = null;
        GatewayRateLimit result = gatewayRateLimitService.addRateLimitPolicy(rateLimit);
        if (result != null) {
            policyId = result.getPolicyId();
        }
        return ResultEntity.ok(policyId);
    }

    /**
     * 编辑流量控制
     *
     * @param policyId     流量控制ID
     * @param policyName   策略名称
     * @param limitQuota   限制数
     * @param intervalUnit 单位时间
     * @param policyType   限流规则类型
     */
    @ApiOperation(value = "编辑流量控制", notes = "编辑流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "接口Id", paramType = "form"),
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "policyType", required = true, value = "限流规则类型:url,origin,user", allowableValues = "url,origin,user", paramType = "form"),
            @ApiImplicitParam(name = "limitQuota", required = true, value = "限制数", paramType = "form"),
            @ApiImplicitParam(name = "intervalUnit", required = true, value = "单位时间:seconds-秒,minutes-分钟,hours-小时,days-天", allowableValues = "seconds,minutes,hours,days", paramType = "form"),
    })
    @PostMapping("/gateway/limit/rate/update")
    public ResultEntity updateRateLimit(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "policyType") String policyType,
            @RequestParam(value = "limitQuota") Long limitQuota,
            @RequestParam(value = "intervalUnit") String intervalUnit
    ) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyId(policyId);
        rateLimit.setPolicyName(policyName);
        rateLimit.setLimitQuota(limitQuota);
        rateLimit.setIntervalUnit(intervalUnit);
        rateLimit.setPolicyType(policyType);
        gatewayRateLimitService.updateRateLimitPolicy(rateLimit);
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }


    /**
     * 移除流量控制
     */
    @ApiOperation(value = "移除流量控制", notes = "移除流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "policyId", paramType = "form"),
    })
    @PostMapping("/gateway/limit/rate/remove")
    public ResultEntity removeRateLimit(
            @RequestParam("policyId") Long policyId) {
        gatewayRateLimitService.removeRateLimitPolicy(policyId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultEntity.ok();
    }
}