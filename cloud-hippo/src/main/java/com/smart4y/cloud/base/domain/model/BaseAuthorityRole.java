package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统权限-角色关联
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_authority_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAuthorityRole extends BaseEntity {

    /**
     * 权限ID
     */
    @Column(name = "authority_id")
    private Long authorityId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 过期时间:null表示长期
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

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
    public BaseAuthorityRole() {
        super();
    }
}
