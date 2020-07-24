package com.smart4y.cloud.base.interfaces.web;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.application.BaseAppService;
import com.smart4y.cloud.base.domain.model.BaseApp;
import com.smart4y.cloud.base.interfaces.command.app.*;
import com.smart4y.cloud.base.interfaces.converter.BaseAppConverter;
import com.smart4y.cloud.base.interfaces.query.BaseAppQuery;
import com.smart4y.cloud.base.interfaces.vo.BaseAppVO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.message.page.Page;
import com.smart4y.cloud.core.security.OpenClientDetails;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.toolkit.base.BeanConvertUtils;
import com.smart4y.cloud.core.dto.OpenClientDetailsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统用户信息
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Api(tags = "系统应用管理")
@RestController
public class BaseAppController {

    @Autowired
    private BaseAppConverter baseAppConverter;
    @Autowired
    private BaseAppService baseAppService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页应用列表
     */
    @ApiOperation(value = "获取分页应用列表", notes = "获取分页应用列表")
    @GetMapping("/app")
    public ResultMessage<Page<BaseAppVO>> getAppListPage(BaseAppQuery query) {
        PageInfo<BaseApp> pageInfo = baseAppService.findListPage(query);
        Page<BaseAppVO> result = baseAppConverter.convertPage(pageInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取应用详情
     */
    @ApiOperation(value = "获取应用详情", notes = "获取应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/{appId}/info")
    public ResultMessage<BaseAppVO> getApp(@PathVariable("appId") String appId) {
        BaseApp appInfo = baseAppService.getAppInfo(appId);
        BaseAppVO result = baseAppConverter.convert(appInfo);
        return ResultMessage.ok(result);
    }

    /**
     * 获取应用开发配置信息
     */
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/client/{clientId}/info")
    public ResultMessage<OpenClientDetailsDTO> getAppClientInfo(@PathVariable("clientId") String clientId) {
        OpenClientDetails clientInfo = baseAppService.getAppClientInfo(clientId);
        OpenClientDetailsDTO detailsDTO = new OpenClientDetailsDTO();
        BeanUtils.copyProperties(clientInfo, detailsDTO);
        return ResultMessage.ok(detailsDTO);
    }

    /**
     * 添加应用信息
     */
    @ApiOperation(value = "添加应用信息", notes = "添加应用信息")
    @PostMapping("/app/add")
    public ResultMessage<String> addApp(@RequestBody AddAppCommand command) {
        BaseApp app = new BaseApp();
        app.setAppName(command.getAppName());
        app.setAppNameEn(command.getAppNameEn());
        app.setAppType(command.getAppType());
        app.setAppOs(command.getAppOs());
        app.setAppIcon(command.getAppIcon());
        app.setAppDesc(command.getAppDesc());
        app.setStatus(command.getStatus());
        app.setWebsite(command.getWebsite());
        app.setDeveloperId(command.getDeveloperId());
        BaseApp result = baseAppService.addAppInfo(app);
        String appId = null;
        if (result != null) {
            appId = result.getAppId();
        }
        return ResultMessage.ok(appId);
    }

    /**
     * 编辑应用信息
     */
    @ApiOperation(value = "编辑应用信息", notes = "编辑应用信息")
    @PostMapping("/app/update")
    public ResultMessage<Void> updateApp(@RequestBody UpdateAppCommand command) {
        BaseApp app = new BaseApp();
        app.setAppId(command.getAppId());
        app.setAppName(command.getAppName());
        app.setAppNameEn(command.getAppNameEn());
        app.setAppType(command.getAppType());
        app.setAppOs(command.getAppOs());
        app.setAppIcon(command.getAppIcon());
        app.setAppDesc(command.getAppDesc());
        app.setStatus(command.getStatus());
        app.setWebsite(command.getWebsite());
        app.setDeveloperId(command.getDeveloperId());
        baseAppService.updateInfo(app);
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }

    /**
     * 完善应用开发信息
     */
    @ApiOperation(value = "完善应用开发信息", notes = "完善应用开发信息")
    @PostMapping("/app/client/update")
    public ResultMessage<String> updateAppClientInfo(@RequestBody UpdateAppClientInfoCommand command) {
        BaseApp app = baseAppService.getAppInfo(command.getAppId());
        OpenClientDetails client = new OpenClientDetails(app.getApiKey(), "",
                command.getScopes(), command.getGrantTypes(), "", command.getRedirectUrls());
        client.setAccessTokenValiditySeconds(command.getAccessTokenValidity());
        client.setRefreshTokenValiditySeconds(command.getRefreshTokenValidity());
        client.setAutoApproveScopes(command.getAutoApproveScopes() != null ? Arrays.asList(command.getAutoApproveScopes().split(",")) : null);
        Map info = BeanConvertUtils.objectToMap(app);
        client.setAdditionalInformation(info);
        baseAppService.updateAppClientInfo(client);
        return ResultMessage.ok();
    }

    /**
     * 重置应用秘钥
     */
    @ApiOperation(value = "重置应用秘钥", notes = "重置应用秘钥")
    @PostMapping("/app/reset")
    public ResultMessage<String> resetAppSecret(@RequestBody ResetAppSecretCommand command) {
        String result = baseAppService.restSecret(command.getAppId());
        return ResultMessage.ok(result);
    }

    /**
     * 删除应用信息
     */
    @ApiOperation(value = "删除应用信息", notes = "删除应用信息")
    @PostMapping("/app/remove")
    public ResultMessage<Void> removeApp(@RequestBody DeleteAppCommand command) {
        baseAppService.removeApp(command.getAppId());
        openRestTemplate.refreshGateway();
        return ResultMessage.ok();
    }
}