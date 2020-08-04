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
 * 权限表
 *
 * @author Youtao on 2020/08/04 11:05
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
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 权限类型（f-文件，m-菜单，e-页面元素，o-功能操作）
     */
    @Column(name = "privilege_type")
    private String privilegeType;

    /**
     * 权限标识
     */
    @Column(name = "authority")
    private String authority;

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
