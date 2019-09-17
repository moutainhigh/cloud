package com.smart4y.cloud.gateway.infrastructure.locator;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.domain.event.RemoteRefreshRouteEvent;
import com.smart4y.cloud.gateway.infrastructure.feign.BaseAuthorityFeign;
import com.smart4y.cloud.gateway.infrastructure.feign.GatewayFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 资源加载器
 * <p>
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class ResourceLocator implements ApplicationListener<RemoteRefreshRouteEvent> {

    /*
     * 单位时间
     */
    /**
     * 1分钟
     */
    public static final long SECONDS_IN_MINUTE = 60;
    /**
     * 一小时
     */
    public static final long SECONDS_IN_HOUR = 3600;
    /**
     * 一天
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
    private Flux<AuthorityResourceDTO> authorityResources;

    /**
     * ip黑名单
     */
    private Flux<IpLimitApiDTO> ipBlacks;

    /**
     * ip白名单
     */
    private Flux<IpLimitApiDTO> ipWhites;

    /**
     * 权限列表
     */
    private Map<String, Collection<ConfigAttribute>> configAttributes = new ConcurrentHashMap<>();
    /**
     * 缓存
     */
    private Map<String, Object> cache = new ConcurrentHashMap<>();


    private RouteDefinitionLocator routeDefinitionLocator;
    private BaseAuthorityFeign baseAuthorityFeign;
    private GatewayFeign gatewayFeign;

    public ResourceLocator() {
        authorityResources = CacheFlux.lookup(cache, "authorityResources", AuthorityResourceDTO.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipBlacks = CacheFlux.lookup(cache, "ipBlacks", IpLimitApiDTO.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipWhites = CacheFlux.lookup(cache, "ipWhites", IpLimitApiDTO.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
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
    public void onApplicationEvent(RemoteRefreshRouteEvent event) {
        refresh();
    }

    /**
     * 获取路由后的地址
     *
     * @return
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
    public Flux<AuthorityResourceDTO> loadAuthorityResources() {
        List<AuthorityResourceDTO> resources = Lists.newArrayList();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        try {
            // 查询所有接口
            resources = baseAuthorityFeign.findAuthorityResource().getData();
            if (resources != null) {
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
                    if (!array.contains(item.getAuthority())) {
                        cfg = new SecurityConfig(item.getAuthority());
                        array.add(cfg);
                    }
                    configAttributes.put(fullPath, array);
                }
                log.info("=============加载动态权限:{}==============", resources.size());
            }
        } catch (Exception e) {
            log.error("加载动态权限错误：{}", e.getLocalizedMessage(), e);
        }
        if (CollectionUtils.isEmpty(resources)) {
            return Flux.empty();
        }
        return Flux.fromIterable(resources);
    }

    /**
     * 加载IP黑名单
     */
    private Flux<IpLimitApiDTO> loadIpBlackList() {
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
        if (CollectionUtils.isEmpty(list)) {
            return Flux.empty();
        }
        return Flux.fromIterable(list);
    }

    /**
     * 加载IP白名单
     */
    private Flux<IpLimitApiDTO> loadIpWhiteList() {
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
        if (CollectionUtils.isEmpty(list)) {
            return Flux.empty();
        }
        return Flux.fromIterable(list);
    }

    public Flux<AuthorityResourceDTO> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(Flux<AuthorityResourceDTO> authorityResources) {
        this.authorityResources = authorityResources;
    }

    public Flux<IpLimitApiDTO> getIpBlacks() {
        return ipBlacks;
    }

    public void setIpBlacks(Flux<IpLimitApiDTO> ipBlacks) {
        this.ipBlacks = ipBlacks;
    }

    public Flux<IpLimitApiDTO> getIpWhites() {
        return ipWhites;
    }

    public void setIpWhites(Flux<IpLimitApiDTO> ipWhites) {
        this.ipWhites = ipWhites;
    }

    public Map<String, Collection<ConfigAttribute>> getConfigAttributes() {
        return configAttributes;
    }

    public void setConfigAttributes(Map<String, Collection<ConfigAttribute>> configAttributes) {
        this.configAttributes = configAttributes;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public void setCache(Map<String, Object> cache) {
        this.cache = cache;
    }
}