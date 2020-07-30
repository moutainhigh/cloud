package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统权限-功能操作关联表
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_authority_element")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAuthorityElement extends BaseEntity<BaseAuthorityElement> {

    /**
     * 操作ID
     */
    @Column(name = "action_id")
    private Long actionId;

    /**
     * API
     */
    @Column(name = "authority_id")
    private Long authorityId;

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
    public BaseAuthorityElement() {
        super();
    }
}