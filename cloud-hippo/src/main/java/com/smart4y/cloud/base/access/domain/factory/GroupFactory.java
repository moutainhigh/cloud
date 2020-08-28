package com.smart4y.cloud.base.access.domain.factory;

import com.smart4y.cloud.base.access.domain.entity.RbacGroup;
import com.smart4y.cloud.core.spring.SpringContextHolder;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeIdWorker;

import java.time.LocalDateTime;

/**
 * @author Youtao on 2020/8/28 11:37
 */
public class GroupFactory {

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
     * 构建新建的集团信息
     *
     * @param groupName  集团名称
     * @param groupState 集团状态
     * @return 集团信息
     */
    public static RbacGroup newGroup(String groupName, String groupState) {
        long groupId = snowflake().nextId();
        return new RbacGroup()
                .setGroupId(groupId)
                .setGroupParentId(0L)
                .setGroupName(groupName)
                .setGroupType("g")
                .setGroupState(groupState)
                .setExistChild(false)
                .setGroupHierarchyId("<" + groupId + "<")
                .setCurrentGroupLeaderId(null)
                .setGroupOrgId(0L)
                .setCreatedDate(LocalDateTime.now());
    }
}