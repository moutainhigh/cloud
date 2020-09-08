package com.smart4y.cloud.access.domain.entity;

import com.smart4y.cloud.mapper.SnowflakeId;
import com.smart4y.cloud.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 权限表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_privilege")
@EqualsAndHashCode(callSuper = true)
public class RbacPrivilege extends BaseEntity<RbacPrivilege> {

    /**
     * 权限ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 权限标识
     */
    @Column(name = "privilege")
    private String privilege;

    /**
     * 权限类型（m-菜单，e-页面元素，o-功能操作）
     */
    @Column(name = "privilege_type")
    private String privilegeType;

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
    public RbacPrivilege() {
        super();
    }
}
