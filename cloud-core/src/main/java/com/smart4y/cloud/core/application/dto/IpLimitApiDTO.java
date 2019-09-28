package com.smart4y.cloud.core.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Data
public class IpLimitApiDTO implements Serializable {

    /**
     * 资源ID
     */
    private Long apiId;

    /**
     * 资源编码
     */
    private String apiCode;

    /**
     * 资源名称
     */
    private String apiName;

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 接口分类
     */
    private String apiCategory;
    /**
     * 资源路径
     */
    private String path;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 资源描述
     */
    private String apiDesc;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 保留数据0-否 1-是 不允许删除
     */
    private Integer isPersist;

    /**
     * 安全认证:0-否 1-是 默认:1
     */
    private Integer isAuth;

    /**
     * 是否公开访问: 0-内部的 1-公开的
     */
    private Integer isOpen;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 响应类型
     */
    private String contentType;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    private Long itemId;
    private Long policyId;
    private String policyName;
    private Integer policyType;
    @JsonIgnore
    private String ipAddress;
    private Set<String> ipAddressSet;

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        if (StringUtils.isNotBlank(ipAddress)) {
            ipAddressSet = new HashSet<>(Arrays.asList(ipAddress.split(";")));
        }
    }
}