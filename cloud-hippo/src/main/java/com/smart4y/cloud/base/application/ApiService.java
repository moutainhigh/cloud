package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseApi;

import java.util.List;

/**
 * 资源应用服务
 *
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
public interface ApiService {

    /**
     * 新增或更新API资源
     */
    void modifyApis(List<BaseApi> apis);

    /**
     * 清理无效权限API
     */
    void clearInvalidApis(String serviceId, List<String> apiCodes);
}