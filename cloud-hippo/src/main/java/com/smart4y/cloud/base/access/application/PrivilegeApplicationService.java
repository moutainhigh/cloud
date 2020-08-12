package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacOperation;
import com.smart4y.cloud.base.access.domain.model.RbacPrivilege;
import com.smart4y.cloud.base.access.interfaces.dtos.privilege.RbacPrivilegePageQuery;
import com.smart4y.cloud.core.message.page.Page;

import java.util.List;

/**
 * @author Youtao on 2020/8/10 12:47
 */
public interface PrivilegeApplicationService {

    /**
     * 系统所有操作权限
     */
    List<RbacOperation> getPrivilegesOperations();

    /**
     * 权限列表分页
     */
    Page<RbacPrivilege> getPrivilegesPage(RbacPrivilegePageQuery query);

    /**
     * 清理无效权限
     *
     * @param serviceId           权限所属服务ID
     * @param validOperationCodes 有效的权限编码列表
     */
    void removeInvalidPrivileges(String serviceId, List<String> validOperationCodes);

    void addNewRolePrivileges(List<String> validOperationCodes);

    /**
     * 新增权限、操作权限
     */
    void addPrivilegeOperations(String serviceId);
}