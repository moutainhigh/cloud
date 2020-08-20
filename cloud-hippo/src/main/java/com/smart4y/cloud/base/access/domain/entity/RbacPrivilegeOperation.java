package com.smart4y.cloud.base.access.domain.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;

/**
 * 权限操作关联表
 *
 * @author Youtao on 2020/08/20 14:47
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_privilege_operation")
@EqualsAndHashCode(callSuper = true)
public class RbacPrivilegeOperation extends BaseEntity<RbacPrivilegeOperation> {

    /**
     * 权限ID
     */
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 操作ID
     */
    @Column(name = "operation_id")
    private Long operationId;

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
    public RbacPrivilegeOperation() {
        super();
    }
}
