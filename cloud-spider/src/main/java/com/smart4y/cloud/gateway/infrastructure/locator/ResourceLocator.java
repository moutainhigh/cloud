package com.smart4y.cloud.gateway.infrastructure.locator;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.domain.ResultEntity;
import com.smart4y.cloud.core.domain.event.RouteRemoteRefreshedEvent;
import com.smart4y.cloud.core.interfaces.AuthorityResourceDTO;
import com.smart4y.cloud.core.interfaces.IpLimitApiDTO;
import com.smart4y.cloud.gateway.infrastructure.feign.BaseAuthorityFeign;
import com.smart4y.cloud.gateway.infrastructure.feign.GatewayFeign;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 资源加载器
 * <p>
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class ResourceLocator implements ApplicationListener<RouteRemoteRefreshedEvent> {

    /**
     * 单位时间：1分钟
     */
    public static final long SECONDS_IN_MINUTE = 60;
    /**
     * 单位时间：一小时
     */
    public static final long SECONDS_IN_HOUR = 3600;
    /**
     * 单位时间：一天
     */
    public static final long SECONDS_IN_DAY = 24 * 3600;

    /**
     * 请求总时长
     */
    public static final int PERIOD_SECOND_TTL = 10;
    public static final int PERIOD_MINUTE_TTL = 2 * 60 + 10;
    public static final int PERIOD_HOUR_TTL = 2 * 3600 + 10;
    public static final int PERIOD_DAY_TTL = 2 * 3600 * 24 + 10;

    /**
     * 权限资源
     */
    @Getter
    @Setter
    private List<AuthorityResourceDTO> authorityResources;

    /**
     * ip黑名单
     */
    @Getter
    @Setter
    private List<IpLimitApiDTO> ipBlacks;

    /**
     * ip白名单
     */
    @Getter
    @Setter
    private List<IpLimitApiDTO> ipWhites;

    /**
     * 权限列表
     */
    @Getter
    @Setter
    private Map<String, Collection<ConfigAttribute>> configAttributes = new ConcurrentHashMap<>();
    /**
     * 缓存
     */
    @Getter
    @Setter
    private Map<String, Object> cache = new ConcurrentHashMap<>();


    private RouteDefinitionLocator routeDefinitionLocator;
    private BaseAuthorityFeign baseAuthorityFeign;
    private GatewayFeign gatewayFeign;

    public ResourceLocator() {
        authorityResources = new CopyOnWriteArrayList<>();
        ipBlacks = new CopyOnWriteArrayList<>();
        ipWhites = new CopyOnWriteArrayList<>();
    }

    public ResourceLocator(RouteDefinitionLocator routeDefinitionLocator, BaseAuthorityFeign baseAuthorityFeign, GatewayFeign gatewayFeign) {
        this();
        this.baseAuthorityFeign = baseAuthorityFeign;
        this.gatewayFeign = gatewayFeign;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * 获取单位时间内刷新时长和请求总时长
     */
    public static long[] getIntervalAndQuota(String timeUnit) {
        if (timeUnit.equalsIgnoreCase(TimeUnit.SECONDS.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_SECOND_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.MINUTES.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_MINUTE_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.HOURS.name())) {
            return new long[]{SECONDS_IN_HOUR, PERIOD_HOUR_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.DAYS.name())) {
            return new long[]{SECONDS_IN_DAY, PERIOD_DAY_TTL};
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

    /**
     * 清空缓存并刷新
     */
    public void refresh() {
        this.configAttributes.clear();
        this.cache.clear();
        this.authorityResources = loadAuthorityResources();
        this.ipBlacks = loadIpBlackList();
        this.ipWhites = loadIpWhiteList();
    }

    @Override
    public void onApplicationEvent(RouteRemoteRefreshedEvent event) {
        refresh();
    }

    /**
     * 获取路由后的地址
     */
    protected String getFullPath(String serviceId, String path) {
        final String[] fullPath = {path.startsWith("/") ? path : "/" + path};
        routeDefinitionLocator.getRouteDefinitions()
                .filter(routeDefinition -> routeDefinition.getId().equals(serviceId))
                .subscribe(routeDefinition -> {
                            routeDefinition.getPredicates().stream()
                                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                                    .filter(predicateDefinition -> !predicateDefinition.getArgs().containsKey("_rateLimit"))
                                    .forEach(predicateDefinition -> {
                                        fullPath[0] = predicateDefinition.getArgs().get("pattern").replace("/**", path.startsWith("/") ? path : "/" + path);
                                    });
                        }
                );
        return fullPath[0];
    }

    /**
     * 加载授权列表
     */
    public List<AuthorityResourceDTO> loadAuthorityResources() {
        List<AuthorityResourceDTO> resources = Lists.newArrayList();
        try {
            // 查询所有接口
            ResultEntity<List<AuthorityResourceDTO>> authorityResourceResponse = baseAuthorityFeign.findAuthorityResource();
            resources = authorityResourceResponse.isOk() ? authorityResourceResponse.getData() : Collections.emptyList();

            ConfigAttribute cfg;
            Collection<ConfigAttribute> array;
            for (AuthorityResourceDTO item : resources) {
                String path = item.getPath();
                if (path == null) {
                    continue;
                }
                String fullPath = getFullPath(item.getServiceId(), path);
                item.setPath(fullPath);
                array = configAttributes.get(fullPath);
                if (array == null) {
                    array = new ArrayList<>();
                }
                cfg = new SecurityConfig(item.getAuthority());
                if (!array.contains(cfg)) {
                    array.add(cfg);
                }
                configAttributes.put(fullPath, array);
            }
            log.info("=============加载动态权限:{}==============", resources.size());
        } catch (Exception e) {
            log.error("加载动态权限错误：{}", e.getLocalizedMessage(), e);
        }

        return resources;
    }

    /**
     * 加载IP黑名单
     */
    private List<IpLimitApiDTO> loadIpBlackList() {
        List<IpLimitApiDTO> list = Lists.newArrayList();
        try {
            list = gatewayFeign.getApiBlackList().getData();
            if (list != null) {
                for (IpLimitApiDTO item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP黑名单:{}==============", list.size());
            }
        } catch (Exception e) {
            log.error("加载IP黑名单错误：{}", e.getLocalizedMessage(), e);
        }

        return list;
    }

    /**
     * 加载IP白名单
     */
    private List<IpLimitApiDTO> loadIpWhiteList() {
        List<IpLimitApiDTO> list = Lists.newArrayList();
        try {
            list = gatewayFeign.getApiWhiteList().getData();
            if (list != null) {
                for (IpLimitApiDTO item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP白名单:{}==============", list.size());
            }
        } catch (Exception e) {
            log.error("加载IP白名单错误：{}", e.getLocalizedMessage(), e);
        }

        return list;
    }
}