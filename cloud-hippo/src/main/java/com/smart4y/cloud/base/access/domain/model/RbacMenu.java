package com.smart4y.cloud.base.access.domain.model;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;
import javax.persistence.Id;

/**
 * 菜单表
 *
 * @author Youtao on 2020/07/30 14:55
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_menu")
@EqualsAndHashCode(callSuper = true)
public class RbacMenu extends BaseEntity<RbacMenu> {

    /**
     * 菜单ID
     */
    @Id
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 菜单父级ID
     */
    @Column(name = "menu_parent_id")
    private Long menuParentId;

    /**
     * 菜单名
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 菜单URL
     */
    @Column(name = "menu_url")
    private String menuUrl;

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
    public RbacMenu() {
        super();
    }
}
