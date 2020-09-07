package com.smart4y.cloud.access.domain.factory;

import com.smart4y.cloud.access.domain.entity.RbacGroup;
import com.smart4y.cloud.access.interfaces.dtos.group.CreateGroupCommand;
import com.smart4y.cloud.core.spring.SpringContextHolder;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeIdWorker;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

/**
 * @author Youtao on 2020/8/28 11:37
 */
public final class GroupFactory {

    /**
     * 主键ID生成器
     */
    private static SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 主键ID生成器
     */
    private static SnowflakeIdWorker snowflake() {
        if (null == snowflakeIdWorker) {
            snowflakeIdWorker = SpringContextHolder.getBean(SnowflakeIdWorker.class);
        }
        return snowflakeIdWorker;
    }

    /**
     * 构建人员信息
     *
     * @param parent  上级
     * @param command 创建信息
     * @return 人员信息
     */
    public static RbacGroup newUser(@NonNull RbacGroup parent, @NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        // 所属机构ID
        long groupOrgId = parent.getGroupOrgId() == 0 ?
                parent.getGroupId() : parent.getGroupOrgId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(parent.getGroupId())
                .setGroupHierarchyId(parent.getGroupHierarchyId() + groupId + "<")
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("u")
                .setExistChild(false)
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
    }

    /**
     * 构建岗位信息
     *
     * @param parent  上级
     * @param command 创建信息
     * @return 岗位信息
     */
    public static RbacGroup newPost(@NonNull RbacGroup parent, @NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        // 所属机构ID
        long groupOrgId = parent.getGroupOrgId() == 0 ?
                parent.getGroupId() : parent.getGroupOrgId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(parent.getGroupId())
                .setGroupHierarchyId(parent.getGroupHierarchyId() + groupId + "<")
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("p")
                .setExistChild(false)
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
    }

    /**
     * 构建小组信息
     *
     * @param parent  上级
     * @param command 创建信息
     * @return 小组信息
     */
    public static RbacGroup newTeam(@NonNull RbacGroup parent, @NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        // 所属机构ID
        long groupOrgId = parent.getGroupOrgId() == 0 ?
                parent.getGroupId() : parent.getGroupOrgId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(parent.getGroupId())
                .setGroupHierarchyId(parent.getGroupHierarchyId() + groupId + "<")
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("t")
                .setExistChild(false)
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
    }

    /**
     * 构建部门信息
     *
     * @param parent  上级
     * @param command 创建信息
     * @return 部门信息
     */
    public static RbacGroup newDepartment(@NonNull RbacGroup parent, @NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        // 所属机构ID
        long groupOrgId = parent.getGroupOrgId() == 0 ?
                parent.getGroupId() : parent.getGroupOrgId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(parent.getGroupId())
                .setGroupHierarchyId(parent.getGroupHierarchyId() + groupId + "<")
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("d")
                .setExistChild(false)
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
    }

    /**
     * 构建公司信息
     *
     * @param parent  上级
     * @param command 创建信息
     * @return 公司信息
     */
    public static RbacGroup newCompany(@Nullable RbacGroup parent, @NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        // 所属机构ID
        long groupParentId = null == parent ? 0 : parent.getGroupId();
        long groupOrgId = null == parent ? 0 : parent.getGroupOrgId();
        String groupHierarchyId = null == parent ? "" : parent.getGroupHierarchyId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(groupParentId)
                .setGroupHierarchyId(groupHierarchyId + groupId + "<")
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("c")
                .setExistChild(false)
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(groupOrgId)
                .setCreatedDate(LocalDateTime.now());
    }

    /**
     * 构建集团信息
     *
     * @param command 创建信息
     * @return 集团信息
     */
    public static RbacGroup newGroup(@NonNull CreateGroupCommand command) {
        long groupId = snowflake().nextId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(0L)
                .setGroupName(command.getGroupName())
                .setGroupState(command.getGroupState())
                .setGroupType("g")
                .setExistChild(false)
                .setGroupHierarchyId("<" + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(0L)
                .setCreatedDate(LocalDateTime.now());
    }
}