package com.smart4y.cloud.base.access.domain.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;

/**
 * 组织角色关联表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_group_role")
@EqualsAndHashCode(callSuper = true)
public class RbacGroupRole extends BaseEntity<RbacGroupRole> {

    /**
     * 组织ID
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

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
    public RbacGroupRole() {
        super();
    }
}