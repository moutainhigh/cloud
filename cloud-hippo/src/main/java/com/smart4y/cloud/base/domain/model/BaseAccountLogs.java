package com.smart4y.cloud.base.domain.model;

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
 * 登录日志
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_account_logs")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseAccountLogs extends BaseEntity<BaseAccountLogs> {

    /**
     * 主键
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "id")
    private Long id;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private LocalDateTime loginTime;

    /**
     * 登录Ip
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 登录设备
     */
    @Column(name = "login_agent")
    private String loginAgent;

    /**
     * 登录次数
     */
    @Column(name = "login_nums")
    private Integer loginNums;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 账号
     */
    @Column(name = "account")
    private String account;

    /**
     * 账号类型
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     * 账号ID
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 账号域
     */
    @Column(name = "domain")
    private String domain;

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
    public BaseAccountLogs() {
        super();
    }
}
