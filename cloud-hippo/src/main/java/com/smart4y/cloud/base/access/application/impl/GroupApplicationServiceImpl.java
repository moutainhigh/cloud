package com.smart4y.cloud.base.access.application.impl;

import com.smart4y.cloud.base.access.application.GroupApplicationService;
import com.smart4y.cloud.base.access.domain.entity.RbacGroup;
import com.smart4y.cloud.base.access.domain.entity.RbacUser;
import com.smart4y.cloud.base.access.domain.factory.GroupFactory;
import com.smart4y.cloud.base.access.domain.service.GroupService;
import com.smart4y.cloud.base.access.domain.service.UserService;
import com.smart4y.cloud.base.access.interfaces.dtos.group.CreateGroupCommand;
import com.smart4y.cloud.core.annotation.ApplicationService;
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

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupApplicationServiceImpl(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public void createGroup(CreateGroupCommand command) {
        String groupType = command.getGroupType();
        switch (groupType) {
            case "g":
                newGroup(command);
                break;
            case "c":
                newCompany(command);
                break;
            case "d":
                newDepartment(command);
                break;
            case "t":
                newTeam(command);
                break;
            case "p":
                newPost(command);
                break;
            case "u":
                newUser(command);
                break;
        }
    }

    private void newUser(CreateGroupCommand command) {
        Long groupParentId = command.getGroupParentId();
        RbacGroup parent = groupService.getById(groupParentId);
        if (!parent.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parent.getGroupId());
        }

        // 添加人员信息
        RbacGroup person = GroupFactory.newUser(parent, command);
        groupService.save(person);

        // 添加组织-人员关联表信息
        long groupId = person.getGroupId();
        groupService.saveGroupUser(groupParentId, Collections.singletonList(groupId));

        // 添加人员表信息
        RbacUser user = new RbacUser()
                .setUserId(groupId)
                .setUserName(command.getGroupName())
                .setCreatedDate(LocalDateTime.now());
        userService.save(user);
    }

    private void newPost(CreateGroupCommand command) {
        RbacGroup parent = groupService.getById(command.getGroupParentId());
        if (!parent.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parent.getGroupId());
        }

        // 添加岗位信息
        RbacGroup post = GroupFactory.newPost(parent, command);
        groupService.save(post);
    }

    private void newTeam(CreateGroupCommand command) {
        RbacGroup parent = groupService.getById(command.getGroupParentId());
        if (!parent.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parent.getGroupId());
        }

        // 添加小组信息
        RbacGroup team = GroupFactory.newTeam(parent, command);
        groupService.save(team);
    }

    private void newDepartment(CreateGroupCommand command) {
        RbacGroup parent = groupService.getById(command.getGroupParentId());
        if (!parent.getExistChild()) {
            // 更新父级子节点状态
            groupService.modifyChildForExist(parent.getGroupId());
        }

        // 添加部门信息
        RbacGroup department = GroupFactory.newDepartment(parent, command);
        groupService.save(department);
    }

    private void newCompany(CreateGroupCommand command) {
        long groupParentId = command.getGroupParentId();
        if (0 == groupParentId) {
            // 公司为顶级
            RbacGroup company = GroupFactory.newCompany(null, command);
            groupService.save(company);
        } else {
            // 集团下的公司
            RbacGroup parentGroup = groupService.getById(groupParentId);
            if (!parentGroup.getExistChild()) {
                // 更新父级子节点状态
                groupService.modifyChildForExist(parentGroup.getGroupId());
            }
            RbacGroup company = GroupFactory.newCompany(parentGroup, command);
            groupService.save(company);
        }
    }

    private void newGroup(CreateGroupCommand command) {
        RbacGroup group = GroupFactory.newGroup(command);
        groupService.save(group);
    }
}