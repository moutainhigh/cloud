package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.GroupApplicationService;
import com.smart4y.cloud.base.access.domain.entity.RbacGroup;
import com.smart4y.cloud.base.access.domain.entity.RbacUser;
import com.smart4y.cloud.base.access.domain.factory.GroupFactory;
import com.smart4y.cloud.base.access.domain.service.GroupService;
import com.smart4y.cloud.base.access.domain.service.UserService;
import com.smart4y.cloud.base.access.interfaces.dtos.group.CreateGroupCommand;
import com.smart4y.cloud.core.annotation.ApplicationService;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Youtao on 2020/8/26 16:26
 */
@Slf4j
@ApplicationService
public class GroupApplicationServiceImpl implements GroupApplicationService {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Override
    public void createGroup(CreateGroupCommand command) {
        String groupType = command.getGroupType();
        switch (groupType) {
            case "g":
                createGroupGroup(command);
                break;
            case "c":
                createGroupCompany(command);
                break;
            case "d":
                createGroupDept(command);
                break;
            case "t":
                createGroupTeam(command);
                break;
            case "p":
                createGroupPost(command);
                break;
            case "u":
                createGroupUser(command);
                break;
        }
    }

    private void createGroupUser(CreateGroupCommand command) {
        Long groupParentId = command.getGroupParentId();
        RbacGroup parentGroup = groupService.getById(groupParentId);
        if (!parentGroup.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parentGroup.getGroupId());
        }

        // 添加人员信息
        // 所属机构ID
        long groupOrgId = parentGroup.getGroupOrgId() == 0 ?
                parentGroup.getGroupId() : parentGroup.getGroupOrgId();
        long groupId = snowflakeIdWorker.nextId();
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(groupParentId)
                .setGroupName(command.getGroupName())
                .setGroupType(command.getGroupType())
                .setGroupState(command.getGroupState())
                .setExistChild(false)
                .setGroupHierarchyId(parentGroup.getGroupHierarchyId() + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
        groupService.save(record);

        // 添加组织-人员关联表信息
        groupService.saveGroupUser(groupParentId, Collections.singletonList(groupId));

        // 添加人员表信息
        RbacUser user = new RbacUser()
                .setUserId(groupId)
                .setUserName(command.getGroupName())
                .setCreatedDate(LocalDateTime.now());
        userService.save(user);
    }

    private void createGroupPost(CreateGroupCommand command) {
        RbacGroup parentGroup = groupService.getById(command.getGroupParentId());
        if (!parentGroup.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parentGroup.getGroupId());
        }

        // 添加岗位信息
        // 所属机构ID
        long groupOrgId = parentGroup.getGroupOrgId() == 0 ?
                parentGroup.getGroupId() : parentGroup.getGroupOrgId();
        long groupId = snowflakeIdWorker.nextId();
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(command.getGroupParentId())
                .setGroupName(command.getGroupName())
                .setGroupType(command.getGroupType())
                .setGroupState(command.getGroupState())
                .setExistChild(false)
                .setGroupHierarchyId(parentGroup.getGroupHierarchyId() + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
        groupService.save(record);
    }

    private void createGroupTeam(CreateGroupCommand command) {
        RbacGroup parentGroup = groupService.getById(command.getGroupParentId());
        if (!parentGroup.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parentGroup.getGroupId());
        }

        // 添加小组信息
        // 组织所属机构ID
        long groupOrgId = parentGroup.getGroupOrgId() == 0 ?
                parentGroup.getGroupId() : parentGroup.getGroupOrgId();
        long groupId = snowflakeIdWorker.nextId();
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(command.getGroupParentId())
                .setGroupName(command.getGroupName())
                .setGroupType(command.getGroupType())
                .setGroupState(command.getGroupState())
                .setExistChild(false)
                .setGroupHierarchyId(parentGroup.getGroupHierarchyId() + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
        groupService.save(record);
    }

    private void createGroupDept(CreateGroupCommand command) {
        RbacGroup parentGroup = groupService.getById(command.getGroupParentId());
        if (!parentGroup.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parentGroup.getGroupId());
        }

        // 添加部门信息
        // 组织所属机构ID
        long groupOrgId = parentGroup.getGroupOrgId() == 0 ?
                parentGroup.getGroupId() : parentGroup.getGroupOrgId();
        long groupId = snowflakeIdWorker.nextId();
        RbacGroup record = new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(command.getGroupParentId())
                .setGroupName(command.getGroupName())
                .setGroupType(command.getGroupType())
                .setGroupState(command.getGroupState())
                .setExistChild(false)
                .setGroupHierarchyId(parentGroup.getGroupHierarchyId() + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
        groupService.save(record);
    }

    private void createGroupCompany(CreateGroupCommand command) {
        long groupParentId = command.getGroupParentId();
        if (0 == groupParentId) {
            // 公司为顶级
            long groupId = snowflakeIdWorker.nextId();
            RbacGroup record = new RbacGroup()
                    .setGroupId(groupId)
                    .setGroupParentId(command.getGroupParentId())
                    .setGroupName(command.getGroupName())
                    .setGroupType(command.getGroupType())
                    .setGroupState(command.getGroupState())
                    .setExistChild(false)
                    .setGroupHierarchyId("<" + groupId + "<")
                    .setCurrentGroupLeaderId(null)
                    .setGroupOrgId(0L)
                    .setCreatedDate(LocalDateTime.now());
            groupService.save(record);
        } else {
            // 集团下的公司
            RbacGroup parentGroup = groupService.getById(groupParentId);
            if (!parentGroup.getExistChild()) {
                // 更新父级子节点状态
                groupService.modifyChildForExist(parentGroup.getGroupId());
            }

            // 添加公司信息
            long groupId = snowflakeIdWorker.nextId();
            RbacGroup record = new RbacGroup()
                    .setGroupId(groupId)
                    .setGroupParentId(command.getGroupParentId())
                    .setGroupName(command.getGroupName())
                    .setGroupType(command.getGroupType())
                    .setGroupState(command.getGroupState())
                    .setExistChild(false)
                    .setGroupHierarchyId(parentGroup.getGroupHierarchyId() + groupId + "<")
                    .setCurrentGroupLeaderId(null)
                    .setGroupOrgId(parentGroup.getGroupId())
                    .setCreatedDate(LocalDateTime.now());
            groupService.save(record);
        }
    }

    private void createGroupGroup(CreateGroupCommand command) {
        RbacGroup group = GroupFactory.newGroup(command.getGroupName(), command.getGroupState());
        groupService.save(group);
    }
}