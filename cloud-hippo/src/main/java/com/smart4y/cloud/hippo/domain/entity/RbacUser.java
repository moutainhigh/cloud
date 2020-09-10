package com.smart4y.cloud.hippo.domain.entity;

import javax.persistence.Column;
import com.smart4y.cloud.core.BaseEntity;
import javax.persistence.Table;
import lombok.experimental.Accessors;
import com.smart4y.cloud.mapper.SnowflakeId;
import tk.mybatis.mapper.annotation.KeySql;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Id;

/**
 * 用户表
 *
 * @author Youtao on 2020/09/10 17:53
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
