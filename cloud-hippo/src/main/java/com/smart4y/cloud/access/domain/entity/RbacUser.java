package com.smart4y.cloud.access.domain.entity;

import com.smart4y.cloud.core.toolkit.gen.SnowflakeId;
import com.smart4y.cloud.mapper.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author Youtao on 2020/08/26 16:31
 */
@Data
@Accessors(chain = true)
@Table(name = "rbac_user")
@EqualsAndHashCode(callSuper = true)
public class RbacUser extends BaseEntity<RbacUser> {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

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
    public RbacUser() {
        super();
    }
}
