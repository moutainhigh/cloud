package com.smart4y.cloud.system.domain.model;

import com.smart4y.cloud.mapper.BaseEntity;
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
 * 系统用户-管理员信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseUser extends BaseEntity<BaseUser> {

    /**
     * 用户ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 登陆账号
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 头像
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 用户类型:super-超级管理员 normal-普通管理员
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 企业ID
     */
    @Column(name = "company_id")
    private Long companyId;

    /**
     * 描述
     */
    @Column(name = "user_desc")
    private String userDesc;

    /**
     * 状态:0-禁用 1-正常 2-锁定
     */
    @Column(name = "status")
    private Integer status;

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
    public BaseUser() {
        super();
    }
}
