package com.smart4y.cloud.base.application;

import com.smart4y.cloud.base.domain.model.BaseApi;
import com.smart4y.cloud.core.domain.IPage;
import com.smart4y.cloud.core.domain.PageParams;

import java.util.List;

/**
 * 接口资源管理
 *
 * @author liuyadu
 */
public interface BaseApiService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseApi> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseApi> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    BaseApi getApi(long apiId);


    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    Boolean isExist(String apiCode);

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    void addApi(BaseApi api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    void updateApi(BaseApi api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    BaseApi getApi(String apiCode);

    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    void removeApi(Long apiId);

    void removeApis(List<Long> apiIds);

    void updateOpenStatusApis(int isOpen, List<Long> apiIds);

    void updateStatusApis(int status, List<Long> apiIds);

    void updateAuthApis(int auth, List<Long> apiIds);
}