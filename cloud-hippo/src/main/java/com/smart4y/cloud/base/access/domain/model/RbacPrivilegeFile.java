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
 * 权限文件关联表
 *
 * @author Youtao on 2020/08/04 11:05
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_privilege_file")
@EqualsAndHashCode(callSuper = true)
public class RbacPrivilegeFile extends BaseEntity<RbacPrivilegeFile> {

    /**
     * 权限ID
     */
    @Id
    @Column(name = "privilege_id")
    private Long privilegeId;

    /**
     * 文件ID
     */
    @Column(name = "file_id")
    private Long fileId;

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
    public RbacPrivilegeFile() {
        super();
    }
}
