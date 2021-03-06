package com.smart4y.cloud.hippo.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.hippo.domain.model.BaseApp;
import com.smart4y.cloud.hippo.interfaces.dtos.BaseAppQuery;
import com.smart4y.cloud.core.security.OpenClientDetails;

/**
 * 应用信息管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseAppService {

    /**
     * 查询应用列表
     */
    PageInfo<BaseApp> findListPage(BaseAppQuery query);

    /**
     * 获取app信息
     */
    BaseApp getAppInfo(String appId);

    /**
     * 获取app和应用信息
     */
    OpenClientDetails getAppClientInfo(String clientId);


    /**
     * 更新应用开发信息
     */
    void updateAppClientInfo(OpenClientDetails client);

    /**
     * 添加应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp addAppInfo(BaseApp app);

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp updateInfo(BaseApp app);


    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    String restSecret(String appId);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    void removeApp(String appId);
}