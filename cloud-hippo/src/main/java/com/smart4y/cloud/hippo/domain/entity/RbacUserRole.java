package com.smart4y.cloud.hippo.domain.entity;

import javax.persistence.Column;
import com.smart4y.cloud.core.BaseEntity;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色关联表
 *
 * @author Youtao on 2020/09/10 17:53
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
     * 构造器
     */
    public RbacUserRole() {
        super();
    }
}
