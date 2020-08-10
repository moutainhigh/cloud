package com.smart4y.cloud.base.access.domain.model;

import javax.persistence.Column;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.EqualsAndHashCode;
import com.smart4y.cloud.mapper.BaseEntity;
import javax.persistence.Id;

/**
 * 菜单表
 *
 * @author Youtao on 2020/08/10 15:26
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
    @KeySql(genId = SnowflakeId.class)
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
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 菜单打开方式（/-路由，http://-HTTP，https://-HTTPs）
     */
    @Column(name = "menu_schema")
    private String menuSchema;

    /**
     * 菜单路径
     */
    @Column(name = "menu_path")
    private String menuPath;

    /**
     * 菜单窗口目标（_self-当前标签，_blank-新标签）
     */
    @Column(name = "menu_target")
    private String menuTarget;

    /**
     * 菜单状态（10-启用，20-禁用，30-锁定）
     */
    @Column(name = "menu_state")
    private String menuState;

    /**
     * 菜单排序
     */
    @Column(name = "menu_sorted")
    private Integer menuSorted;

    /**
     * 菜单是否存在子节点（0-不存在 1-存在）
     */
    @Column(name = "exist_child")
    private Boolean existChild;

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
