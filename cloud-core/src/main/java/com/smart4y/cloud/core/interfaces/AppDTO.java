package com.smart4y.cloud.core.interfaces;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统应用-基础信息
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AppDTO implements Serializable {

    /**
     * 应用ID
     */
    private String appId;
    /**
     * API访问key
     */
    private String apiKey;
    /**
     * API访问密钥
     */
    private String secretKey;
    /**
     * app类型：server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用
     */
    private String appType;
    /**
     * 应用图标
     */
    private String appIcon;
    /**
     * app名称
     */
    private String appName;
    /**
     * app英文名称
     */
    private String appNameEn;
    /**
     * 移动应用操作系统：ios-苹果 android-安卓
     */
    private String appOs;
    /**
     * 用户ID:默认为0
     */
    private Long developerId;
    /**
     * app描述
     */
    private String appDesc;
    /**
     * 官方网址
     */
    private String website;
    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;
    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;
}