package com.smart4y.cloud.access.domain.entity;

import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 权限菜单关联表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_privilege_menu")
@EqualsAndHashCode(callSuper = true)
public class RbacPrivilegeMenu extends BaseEntity<RbacPrivilegeMenu> {

    /**
     * 权限ID
     */
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    private Long menuId;

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
    public RbacPrivilegeMenu() {
        super();
    }
}
