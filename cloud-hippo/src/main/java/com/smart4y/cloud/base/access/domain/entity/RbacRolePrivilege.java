package com.smart4y.cloud.base.access.domain.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;

/**
 * 角色权限关联表
 *
 * @author Youtao on 2020/08/11 15:58
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_role_privilege")
@EqualsAndHashCode(callSuper = true)
public class RbacRolePrivilege extends BaseEntity<RbacRolePrivilege> {

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限ID
     */
    @Column(name = "privilege_id")
    private Long privilegeId;

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
    public RbacRolePrivilege() {
        super();
    }
}
