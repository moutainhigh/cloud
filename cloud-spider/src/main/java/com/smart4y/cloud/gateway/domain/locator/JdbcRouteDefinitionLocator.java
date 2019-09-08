package com.smart4y.cloud.gateway.domain.locator;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.application.dto.GatewayRouteDTO;
import com.smart4y.cloud.core.application.dto.RateLimitApiDTO;
import com.smart4y.cloud.core.domain.RemoteRefreshRouteEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义动态路由加载器
 * <p>
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class JdbcRouteDefinitionLocator implements RouteDefinitionLocator, ApplicationListener<RemoteRefreshRouteEvent> {

    private final static String SELECT_ROUTES = "SELECT * FROM gateway_route WHERE status = 1";
    private final static String SELECT_LIMIT_PATH = "" +
            "SELECT " +
            "        i.policy_id, " +
            "        p.limit_quota, " +
            "        p.interval_unit, " +
            "        p.policy_name, " +
            "        a.api_id, " +
            "        a.api_code, " +
            "        a.api_name, " +
            "        a.api_category, " +
            "        a.service_id, " +
            "        a.path, " +
            "        r.url " +
            "FROM " +
            "        gateway_rate_limit_api AS i " +
            "INNER JOIN gateway_rate_limit AS p ON i.policy_id = p.policy_id " +
            "INNER JOIN base_api AS a ON i.api_id = a.api_id " +
            "INNER JOIN gateway_route AS r ON a.service_id = r.route_name " +
            "WHERE p.policy_type = 'url'";
    private JdbcTemplate jdbcTemplate;
    private Flux<RouteDefinition> routeDefinitions;
    private Map<String, List> cache = new ConcurrentHashMap<>();


    public JdbcRouteDefinitionLocator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        routeDefinitions = CacheFlux.lookup(cache, "routeDefs", RouteDefinition.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
    }

    /**
     * Clears the cache of routeDefinitions.
     */
    public void refresh() {
        this.cache.clear();
        this.routeDefinitions = this.loadRoutes();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return this.routeDefinitions;
    }

    @Override
    public void onApplicationEvent(RemoteRefreshRouteEvent event) {
        refresh();
    }

    protected String getFullPath(List<GatewayRouteDTO> routeList, String serviceId, String path) {
        final String[] fullPath = {path.startsWith("/") ? path : "/" + path};
        if (routeList != null) {
            routeList.forEach(route -> {
                if (route.getServiceId() != null && route.getServiceId().equals(serviceId)) {
                    fullPath[0] = route.getPath().replace("/**", path.startsWith("/") ? path : "/" + path);
                }
            });
        }
        return fullPath[0];
    }

    /**
     * 动态加载路由
     * * 示例
     * id: opencloud-admin-provider
     * uri: lb://opencloud-admin-provider
     * predicates:
     * - Path=/admin/**
     * - Name=平台后台管理服务
     * filters:
     * #转发去掉前缀,总要否则swagger无法加载
     * - StripPrefix=1
     */
    private Flux<RouteDefinition> loadRoutes() {
        //从数据库拿到路由配置
        List<RouteDefinition> routes = Lists.newArrayList();
        try {
            List<GatewayRouteDTO> routeList = jdbcTemplate.query(SELECT_ROUTES, (rs, i) -> {
                GatewayRouteDTO result = new GatewayRouteDTO();
                result.setRouteId(rs.getLong("route_id"));
                result.setPath(rs.getString("path"));
                result.setServiceId(rs.getString("service_id"));
                result.setUrl(rs.getString("url"));
                result.setStatus(rs.getInt("status"));
                result.setRetryable(rs.getInt("retryable"));
                result.setStripPrefix(rs.getInt("strip_prefix"));
                result.setIsPersist(rs.getInt("is_persist"));
                result.setRouteName(rs.getString("route_name"));
                return result;
            });
            List<RateLimitApiDTO> limitApiList = jdbcTemplate.query(SELECT_LIMIT_PATH, (rs, i) -> {
                RateLimitApiDTO result = new RateLimitApiDTO();
                result.setPolicyId(rs.getLong("policy_id"));
                result.setPolicyName(rs.getString("policy_name"));
                result.setServiceId(rs.getString("service_id"));
                result.setPath(rs.getString("path"));
                result.setApiId(rs.getLong("api_id"));
                result.setApiCode(rs.getString("api_code"));
                result.setApiName(rs.getString("api_name"));
                result.setApiCategory(rs.getString("api_category"));
                result.setLimitQuota(rs.getLong("limit_quota"));
                result.setIntervalUnit(rs.getString("interval_unit"));
                result.setUrl(rs.getString("url"));
                return result;
            });
            if (CollectionUtils.isNotEmpty(limitApiList)) {
                // 加载限流
                limitApiList.forEach(item -> {
                    long[] arry = ResourceLocator.getIntervalAndQuota(item.getIntervalUnit());
                    Long refreshInterval = arry[0];
                    Long quota = arry[1];
                    // 允许用户每秒处理多少个请求
                    long replenishRate = item.getLimitQuota() / refreshInterval;
                    replenishRate = replenishRate < 1 ? 1 : refreshInterval;
                    // 令牌桶的容量，允许在一秒钟内完成的最大请求数
                    long burstCapacity = replenishRate * 2;
                    RouteDefinition definition = new RouteDefinition();
                    List<PredicateDefinition> predicates = Lists.newArrayList();
                    List<FilterDefinition> filters = Lists.newArrayList();
                    definition.setId(item.getApiId().toString());
                    PredicateDefinition predicatePath = new PredicateDefinition();
                    String fullPath = getFullPath(routeList, item.getServiceId(), item.getPath());
                    Map<String, String> predicatePathParams = new HashMap<>(8);
                    predicatePath.setName("Path");
                    predicatePathParams.put("pattern", fullPath);
                    predicatePathParams.put("pathPattern", fullPath);
                    predicatePathParams.put("_rateLimit", "1");
                    predicatePath.setArgs(predicatePathParams);
                    predicates.add(predicatePath);

                    // 服务地址
                    URI uri = UriComponentsBuilder.fromUriString(StringUtils.isNotBlank(item.getUrl()) ? item.getUrl() : "lb://" + item.getServiceId()).build().toUri();

                    // 路径去前缀
                    FilterDefinition stripPrefixDefinition = new FilterDefinition();
                    Map<String, String> stripPrefixParams = new HashMap<>(8);
                    stripPrefixDefinition.setName("StripPrefix");
                    stripPrefixParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
                    stripPrefixDefinition.setArgs(stripPrefixParams);
                    filters.add(stripPrefixDefinition);
                    // 限流
                    FilterDefinition rateLimiterDefinition = new FilterDefinition();
                    Map<String, String> rateLimiterParams = new HashMap<>(8);
                    rateLimiterDefinition.setName("RequestRateLimiter");
                    // 令牌桶流速
                    rateLimiterParams.put("redis-rate-limiter.replenishRate", String.valueOf(replenishRate));
                    // 令牌桶容量
                    rateLimiterParams.put("redis-rate-limiter.burstCapacity", String.valueOf(burstCapacity));
                    // 限流策略(#{@BeanName})
                    rateLimiterParams.put("key-resolver", "#{@pathKeyResolver}");
                    rateLimiterDefinition.setArgs(rateLimiterParams);
                    filters.add(rateLimiterDefinition);

                    definition.setPredicates(predicates);
                    definition.setFilters(filters);
                    definition.setUri(uri);
                    routes.add(definition);
                });
            }
            if (CollectionUtils.isNotEmpty(routeList)) {
                // 最后加载路由
                routeList.forEach(gatewayRoute -> {
                    RouteDefinition definition = new RouteDefinition();
                    List<PredicateDefinition> predicates = Lists.newArrayList();
                    List<FilterDefinition> filters = Lists.newArrayList();
                    definition.setId(gatewayRoute.getRouteName());
                    // 路由地址
                    PredicateDefinition predicatePath = new PredicateDefinition();
                    Map<String, String> predicatePathParams = new HashMap<>(8);
                    predicatePath.setName("Path");
                    predicatePathParams.put("name", StringUtils.isBlank(gatewayRoute.getRouteName()) ? gatewayRoute.getRouteId().toString() : gatewayRoute.getRouteName());
                    predicatePathParams.put("pattern", gatewayRoute.getPath());
                    predicatePathParams.put("pathPattern", gatewayRoute.getPath());
                    predicatePath.setArgs(predicatePathParams);
                    predicates.add(predicatePath);
                    // 服务地址
                    URI uri = UriComponentsBuilder.fromUriString(StringUtils.isNotBlank(gatewayRoute.getUrl()) ? gatewayRoute.getUrl() : "lb://" + gatewayRoute.getServiceId()).build().toUri();

                    FilterDefinition stripPrefixDefinition = new FilterDefinition();
                    Map<String, String> stripPrefixParams = new HashMap<>(8);
                    stripPrefixDefinition.setName("StripPrefix");
                    stripPrefixParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
                    stripPrefixDefinition.setArgs(stripPrefixParams);
                    filters.add(stripPrefixDefinition);

                    definition.setPredicates(predicates);
                    definition.setFilters(filters);
                    definition.setUri(uri);
                    routes.add(definition);
                });
            }
            log.info("=============加载动态路由:{}==============", routeList.size());
            log.info("=============加载动态限流:{}==============", limitApiList.size());
        } catch (Exception e) {
            log.error("加载动态路由错误:{}", e.getLocalizedMessage(), e);
        }
        return Flux.fromIterable(routes);
    }
}