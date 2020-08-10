package com.smart4y.cloud.base.access.domain.model;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;

/**
 * 用户角色关联表
 *
 * @author Youtao on 2020/08/10 15:26
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_user_role")
@EqualsAndHashCode(callSuper = true)
public class RbacUserRole extends BaseEntity<RbacUserRole> {

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

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
    public RbacUserRole() {
        super();
    }
}
