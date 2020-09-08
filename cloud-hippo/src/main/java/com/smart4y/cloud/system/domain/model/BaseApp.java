package com.smart4y.cloud.system.domain.model;

import com.smart4y.cloud.core.BaseEntity;
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
 * 系统应用-基础信息
 *
 * @author Youtao
 *         Created by youtao on 2019/09/17.
 */
@Data
@Table(name = "base_app")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BaseApp extends BaseEntity<BaseApp> {

    /**
     * 客户端ID
     */
    @Id
    @KeySql(genId = SnowflakeId.class)
    @Column(name = "app_id")
    private String appId;

    /**
     * API访问key
     */
    @Column(name = "api_key")
    private String apiKey;

    /**
     * API访问密钥
     */
    @Column(name = "secret_key")
    private String secretKey;

    /**
     * app名称
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * app英文名称
     */
    @Column(name = "app_name_en")
    private String appNameEn;

    /**
     * 应用图标
     */
    @Column(name = "app_icon")
    private String appIcon;

    /**
     * app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    @Column(name = "app_type")
    private String appType;

    /**
     * app描述
     */
    @Column(name = "app_desc")
    private String appDesc;

    /**
     * 移动应用操作系统:ios-苹果 android-安卓
     */
    @Column(name = "app_os")
    private String appOs;

    /**
     * 官网地址
     */
    @Column(name = "website")
    private String website;

    /**
     * 开发者ID:默认为0
     */
    @Column(name = "developer_id")
    private Long developerId;

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
    public BaseApp() {
        super();
    }
}
