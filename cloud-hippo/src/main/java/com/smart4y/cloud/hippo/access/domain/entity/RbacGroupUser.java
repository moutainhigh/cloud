package com.smart4y.cloud.hippo.access.domain.entity;

import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 组织用户关联表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_group_user")
@EqualsAndHashCode(callSuper = true)
public class RbacGroupUser extends BaseEntity<RbacGroupUser> {

    /**
     * 组织ID
     */
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

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
    public RbacGroupUser() {
        super();
    }
}
