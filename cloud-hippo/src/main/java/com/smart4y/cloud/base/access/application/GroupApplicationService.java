package com.smart4y.cloud.base.access.application;

import com.smart4y.cloud.base.access.domain.model.RbacGroup;
import com.smart4y.cloud.base.access.domain.model.RbacRole;
import com.smart4y.cloud.base.access.domain.model.RbacUser;
import com.smart4y.cloud.base.access.interfaces.dtos.group.RbacGroupPageQuery;
import com.smart4y.cloud.base.access.interfaces.dtos.group.RbacGroupQuery;
import com.smart4y.cloud.core.message.page.Page;

import java.util.List;

/**
 * @author Youtao
 * on 2020/7/31 15:49
 */
public interface GroupApplicationService {

    List<RbacGroup> getGroupChildren(RbacGroupQuery query);

    Page<RbacGroup> getGroupsPage(RbacGroupPageQuery query);

    RbacGroup viewGroup(long groupId);

    List<RbacUser> getGroupUsers(long groupId);

    List<RbacRole> getGroupRoles(long groupId);
}