package com.smart4y.cloud.core.infrastructure.mapper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 实体基类
 * <code>
 * 批量添加字段方法：
 * alter table base_resource_menu add (
 * `create_date` datetime COMMENT '创建时间',
 * `last_modified_date` datetime DEFAULT NULL COMMENT '最后修改时间',
 * `remove_date` datetime DEFAULT NULL COMMENT '逻辑删除时间',
 * `version` bigint(20) DEFAULT '0' COMMENT '版本号',
 * `archived` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否存档（0删除 1存档）'
 * )
 * </code>
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * 构造器
     */
    protected BaseEntity() {
    }
}