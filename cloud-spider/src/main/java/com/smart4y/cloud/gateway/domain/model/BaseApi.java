package com.smart4y.cloud.gateway.domain.model;

import com.smart4y.cloud.core.mapper.BaseEntity;
import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统资源-API接口
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_api")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseApi extends BaseEntity<BaseApi> {

    /**
     * 接口ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "api_id")
    private Long apiId;

    /**
     * 接口编码
     */
    @Column(name = "api_code")
    private String apiCode;

    /**
     * 接口名称
     */
    @Column(name = "api_name")
    private String apiName;

    /**
     * 接口分类:default-默认分类
     */
    @Column(name = "api_category")
    private String apiCategory;

    /**
     * 资源描述
     */
    @Column(name = "api_desc")
    private String apiDesc;

    /**
     * 请求方式
     */
    @Column(name = "request_method")
    private String requestMethod;

    /**
     * 响应类型
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 请求路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 状态:0-无效 1-有效
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    @Column(name = "is_persist")
    private Integer isPersist;

    /**
     * 是否需要认证: 0-无认证 1-身份认证 默认:1
     */
    @Column(name = "is_auth")
    private Integer isAuth;

    /**
     * 是否公开: 0-内部的 1-公开的
     */
    @Column(name = "is_open")
    private Integer isOpen;

    /**
     * 类名
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 方法名
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /**
     * 最后修改时间
     */
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    /**
     * 构造器
     */
    public BaseApi() {
        super();
    }
}
