package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统资源-菜单信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_menu")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseMenu extends BaseEntity<BaseMenu> {

    /**
     * 菜单Id
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 父级菜单
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 描述
     */
    @Column(name = "menu_desc")
    private String menuDesc;

    /**
     * 路径前缀
     */
    @Column(name = "scheme")
    private String scheme;

    /**
     * 请求路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 菜单标题
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 打开方式:_self窗口内,_blank新窗口
     */
    @Column(name = "target")
    private String target;

    /**
     * 优先级 越小越靠前
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 状态:0-无效 1-有效
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 服务名
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist")
    private Integer isPersist;

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
    public BaseMenu() {
        super();
    }
}
