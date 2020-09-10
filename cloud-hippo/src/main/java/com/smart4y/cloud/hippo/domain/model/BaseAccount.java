package com.smart4y.cloud.hippo.domain.model;

import com.smart4y.cloud.core.BaseEntity;
import com.smart4y.cloud.mapper.SnowflakeId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 登录账号
 *
 * @author Youtao
 * Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAccount extends BaseEntity<BaseAccount> {

    /**
     * 主键
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 用户Id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 标识：手机号、邮箱、 用户名、或第三方应用的唯一标识
     */
    @Column(name = "account")
    private String account;

    /**
     * 密码凭证：站内的保存密码、站外的不保存或保存token）
     */
    @Column(name = "password")
    private String password;

    /**
     * 登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 账户域:@admin.com,@developer.com
     */
    @Column(name = "domain")
    private String domain;

    /**
     * 注册IP
     */
    @Column(name = "register_ip")
    private String registerIp;

    /**
     * 状态:0-禁用 1-启用 2-锁定
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 注册时间
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
    public BaseAccount() {
        super();
    }
}
