package com.smart4y.cloud.hippo.domain.entity;

import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 权限操作关联表
 *
 * @author Youtao on 2020/08/26 16:31
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
