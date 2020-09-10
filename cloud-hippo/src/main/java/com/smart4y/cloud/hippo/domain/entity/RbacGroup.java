package com.smart4y.cloud.hippo.domain.entity;

import javax.persistence.Column;
import com.smart4y.cloud.core.BaseEntity;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import com.smart4y.cloud.mapper.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Id;

/**
 * 组织结构表
 *
 * @author Youtao on 2020/09/10 17:53
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
     * 组织类型（g-集团，c-公司，d-部门，t-小组，p-岗位，u-人员，r-角色线）
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
