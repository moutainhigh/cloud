package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.interfaces.dtos.group.CreateGroupCommand;

/**
 * @author Youtao on 2020/8/26 16:25
 */
public interface GroupApplicationService {

    /**
     * 添加组织结构
     *
     * @param command 组织结构信息
     */
    void createGroup(CreateGroupCommand command);

    /**
     * 删除组织结构
     *
     * @param groupId 组织ID
     */
    void removeGroup(long groupId);
}