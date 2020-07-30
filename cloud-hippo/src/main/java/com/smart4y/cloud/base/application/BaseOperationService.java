package com.smart4y.cloud.base.application;

import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.base.domain.model.BaseOperation;
import com.smart4y.cloud.base.interfaces.query.BaseResourceQuery;

import java.util.List;

/**
 * 接口资源管理
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public interface BaseOperationService {

    /**
     * 分页查询
     */
    PageInfo<BaseOperation> findListPage(BaseResourceQuery query);

    /**
     * 查询列表
     */
    List<BaseOperation> findAllList(String serviceId);

    /**
     * 根据主键获取接口
     */
    BaseOperation getApi(long apiId);

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
    void addApi(BaseOperation api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    void updateApi(BaseOperation api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    BaseOperation getApi(String apiCode);

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