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
 * 系统权限-菜单权限、操作权限、API权限
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_authority")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAuthority extends BaseEntity<BaseAuthority> {

    /**
     * 权限ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "authority_id")
    private Long authorityId;

    /**
     * 权限标识
     */
    @Column(name = "authority")
    private String authority;

    /**
     * 菜单资源ID
     */
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * API资源ID
     */
    @Column(name = "api_id")
    private Long apiId;

    /**
     * 资源ID
     */
    @Column(name = "action_id")
    private Long actionId;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 修改时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * 构造器
     */
    public BaseAuthority() {
        super();
    }
}
