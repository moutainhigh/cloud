package com.smart4y.cloud.base.access.domain.model;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;
import javax.persistence.Id;

/**
 * 组织表
 *
 * @author Youtao on 2020/08/10 15:26
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
     * 组织父级ID
     */
    @Column(name = "group_parent_id")
    private Long groupParentId;

    /**
     * 组织名
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 组织类型（g-集团，c-公司，d-部门，t-小组）
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
