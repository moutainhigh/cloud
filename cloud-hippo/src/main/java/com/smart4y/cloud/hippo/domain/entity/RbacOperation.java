package com.smart4y.cloud.hippo.domain.entity;

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
 * 功能操作表
 *
 * @author Youtao on 2020/08/26 16:31
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
     * 操作编码
     */
    @Column(name = "operation_code")
    private String operationCode;

    /**
     * 操作名
     */
    @Column(name = "operation_name")
    private String operationName;

    /**
     * 操作描述
     */
    @Column(name = "operation_desc")
    private String operationDesc;

    /**
     * 操作拦截的URL前缀
     */
    @Column(name = "operation_path")
    private String operationPath;

    /**
     * 操作方法类型（GET，POST，PUT，DELETE等）
     */
    @Column(name = "operation_method")
    private String operationMethod;

    /**
     * 操作是否需要认证（0不需要 1需要）
     */
    @Column(name = "operation_auth")
    private Boolean operationAuth;

    /**
     * 操作是否对外开放（0不开放 1开放）
     */
    @Column(name = "operation_open")
    private Boolean operationOpen;

    /**
     * 操作状态（10-启用，20-禁用，30-锁定）
     */
    @Column(name = "operation_state")
    private String operationState;

    /**
     * 操作所属服务ID
     */
    @Column(name = "operation_service_id")
    private String operationServiceId;

    /**
     * 操作方法名
     */
    @Column(name = "operation_method_name")
    private String operationMethodName;

    /**
     * 操作媒体类型
     */
    @Column(name = "operation_content_type")
    private String operationContentType;

    /**
     * 操作全限定类名
     */
    @Column(name = "operation_class_name")
    private String operationClassName;

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
