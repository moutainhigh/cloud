package com.smart4y.cloud.base.domain.model;

import com.smart4y.cloud.core.infrastructure.mapper.BaseEntity;
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 系统资源-功能操作
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_action")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAction extends BaseEntity {

    /**
     * 资源ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "action_id")
    private Long actionId;

    /**
     * 资源编码
     */
    @Column(name = "action_code")
    private String actionCode;

    /**
     * 资源名称
     */
    @Column(name = "action_name")
    private String actionName;

    /**
     * 资源描述
     */
    @Column(name = "action_desc")
    private String actionDesc;

    /**
     * 资源父节点
     */
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 优先级 越小越靠前
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
     * 服务名称
     */
    @Column(name = "service_id")
    private String serviceId;

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
    public BaseAction() {
        super();
    }
}
