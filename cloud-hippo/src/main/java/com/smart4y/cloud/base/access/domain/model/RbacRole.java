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
 * 角色表
 *
 * @author Youtao on 2020/08/10 10:52
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_role")
@EqualsAndHashCode(callSuper = true)
public class RbacRole extends BaseEntity<RbacRole> {

    /**
     * 角色ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;

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
    public RbacRole() {
        super();
    }
}
