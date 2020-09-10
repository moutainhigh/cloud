package com.smart4y.cloud.hippo.domain.entity;

import javax.persistence.Column;
import com.smart4y.cloud.core.BaseEntity;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织角色关联表
 *
 * @author Youtao on 2020/09/10 17:53
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
     * 构造器
     */
    public RbacGroupRole() {
        super();
    }
}
