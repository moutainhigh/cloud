package com.smart4y.cloud.access.domain.entity;

import com.smart4y.cloud.mapper.SnowflakeId;
import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 组织结构表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_group")
@EqualsAndHashCode(callSuper = true)
public class RbacGroup extends BaseEntity<RbacGroup> {

    /**
     * 组织ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 组织名
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 组织类型（g-集团，c-公司，d-部门，t-小组，p-岗位，u-人员）
     */
    @Column(name = "group_type")
    private String groupType;

    /**
     * 组织状态（10-启用，20-禁用，30-锁定）
     */
    @Column(name = "group_state")
    private String groupState;

    /**
     * 组织是否存在子节点（0-不存在 1-存在）
     */
    @Column(name = "exist_child")
    private Boolean existChild;

    /**
     * 组织父级ID
     */
    @Column(name = "group_parent_id")
    private Long groupParentId;

    /**
     * 组织所属机构ID
     */
    @Column(name = "group_org_id")
    private Long groupOrgId;

    /**
     * 当前组织领导ID
     */
    @Column(name = "current_group_leader_id")
    private Long currentGroupLeaderId;

    /**
     * 组织层级ID（例如：冒险元素<谛听<技术部<技术研发中心）
     */
    @Column(name = "group_hierarchy_id")
    private String groupHierarchyId;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 更新时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;


    /**
     * 构造器
     */
    public RbacGroup() {
        super();
    }
}
