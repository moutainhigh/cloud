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
 * 功能操作表
 *
 * @author Youtao on 2020/08/06 16:01
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_operation")
@EqualsAndHashCode(callSuper = true)
public class RbacOperation extends BaseEntity<RbacOperation> {

    /**
     * 操作ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "operation_id")
    private Long operationId;

    /**
     * 操作父级ID
     */
    @Column(name = "operation_parent_id")
    private Long operationParentId;

    /**
     * 操作拦截的URL前缀
     */
    @Column(name = "operation_path")
    private String operationPath;

    /**
     * 操作名
     */
    @Column(name = "operation_name")
    private String operationName;

    /**
     * 操作编码
     */
    @Column(name = "operation_no")
    private String operationNo;

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
    public RbacOperation() {
        super();
    }
}
