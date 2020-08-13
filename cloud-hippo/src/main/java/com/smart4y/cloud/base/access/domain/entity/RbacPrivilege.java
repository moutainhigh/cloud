package com.smart4y.cloud.base.access.domain.entity;

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
 * 权限表
 *
 * @author Youtao on 2020/08/11 15:58
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
