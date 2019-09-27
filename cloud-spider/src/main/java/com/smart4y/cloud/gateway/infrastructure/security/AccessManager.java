package com.smart4y.cloud.gateway.infrastructure.security;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.application.dto.IpLimitApiDTO;
import com.smart4y.cloud.core.domain.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import com.smart4y.cloud.core.infrastructure.toolkit.StringUtils;
import com.smart4y.cloud.gateway.infrastructure.locator.ResourceLocator;
import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import com.smart4y.cloud.gateway.infrastructure.toolkit.ReactiveIpAddressMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

/**
 * 访问权限控制管理类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
@Component
public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final AntPathMatcher pathMatch = new AntPathMatcher();
    private ResourceLocator resourceLocator;
    private ApiProperties apiProperties;
    private Set<String> permitAll = new ConcurrentHashSet<>();
    private Set<String> authorityIgnores = new ConcurrentHashSet<>();

    public AccessManager(ResourceLocator resourceLocator, ApiProperties apiProperties) {
        this.resourceLocator = resourceLocator;
        this.apiProperties = apiProperties;
        // 默认放行
        permitAll.add("/");
        permitAll.add("/error");
        permitAll.add("/favicon.ico");
        if (apiProperties != null) {
            if (apiProperties.getPermitAll() != null) {
                permitAll.addAll(apiProperties.getPermitAll());
            }
            if (apiProperties.isApiDebug()) {
                permitAll.add("/**/v2/api-docs/**");
                permitAll.add("/**/swagger-resources/**");
                permitAll.add("/webjars/**");
                permitAll.add("/doc.html");
                permitAll.add("/swagger-ui.html");
            }
            if (apiProperties.getAuthorityIgnores() != null) {
                authorityIgnores.addAll(apiProperties.getAuthorityIgnores());
            }
        }
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String requestPath = exchange.getRequest().getURI().getPath();
        if (!apiProperties.isAccessControl()) {
            return Mono.just(new AuthorizationDecision(true));
        }
        // 是否直接放行
        if (permitAll(requestPath)) {
            return Mono.just(new AuthorizationDecision(true));
        }
        return authentication
                .map(a -> new AuthorizationDecision(checkAuthorities(exchange, a, requestPath)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    /**
     * 始终放行
     */
    public boolean permitAll(String requestPath) {
        final Boolean[] result = {false};
        for (String path : permitAll) {
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        // 动态权限列表
        Flux<AuthorityResourceDTO> resources = resourceLocator.getAuthorityResources();
        resources.filter(res -> StringUtils.isNotBlank(res.getPath()))
                .subscribe(res -> {
                    boolean isAuth = res.getIsAuth() != null && res.getIsAuth().intValue() == 1;
                    // 无需认证,返回true
                    if (pathMatch.match(res.getPath(), requestPath) && !isAuth) {
                        result[0] = true;
                    }
                });
        return result[0];
    }

    /**
     * 获取资源状态
     */
    public AuthorityResourceDTO getResource(String requestPath) {
        final AuthorityResourceDTO[] result = {null};
        // 动态权限列表
        Flux<AuthorityResourceDTO> resources = resourceLocator.getAuthorityResources();
        resources.filter(r -> !"/**".equals(r.getPath()) && !permitAll(requestPath) && StringUtils.isNotBlank(r.getPath()) && pathMatch.match(r.getPath(), requestPath))
                .subscribe(r -> result[0] = r);
        return result[0];
    }

    /**
     * 忽略鉴权
     */
    private boolean authorityIgnores(String requestPath) {
        for (String path : authorityIgnores) {
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查权限
     */
    private boolean checkAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
        Object principal = authentication.getPrincipal();
        // 已认证身份
        if (principal != null) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                //check if this uri can be access by anonymous
                //return
            }
            if (authorityIgnores(requestPath)) {
                // 认证通过,并且无需权限
                return true;
            }
            return mathAuthorities(exchange, authentication, requestPath);
        }
        return false;
    }

    public boolean mathAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
        Collection<ConfigAttribute> attributes = getAttributes(requestPath);
        int result = 0;
        int expires = 0;
        if (authentication == null) {
            return false;
        } else {
            if (CommonConstants.ROOT.equals(authentication.getName())) {
                // 默认超级管理员账号,直接放行
                return true;
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (ConfigAttribute attribute : attributes) {
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        result++;
                        if (authority instanceof OpenAuthority) {
                            OpenAuthority customer = (OpenAuthority) authority;
                            if (customer.getIsExpired()) {
                                // 授权过期数
                                expires++;
                            }
                        }
                    }
                }
            }
            log.debug("mathAuthorities result[{}] expires[{}]", result, expires);
            if (expires > 0) {
                // 授权已过期
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage());
            }
            return result > 0;
        }
    }

    private Collection<ConfigAttribute> getAttributes(String requestPath) {
        // 匹配动态权限
        for (String url : resourceLocator.getConfigAttributes().keySet()) {
            // 防止匹配错误 忽略/**
            if (!"/**".equals(url) && pathMatch.match(url, requestPath)) {
                // 返回匹配到权限
                return resourceLocator.getConfigAttributes().get(url);
            }
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }

    /**
     * IP黑名单验证
     */
    public boolean matchIpOrOriginBlacklist(String requestPath, String ipAddress, String origin) {
        final Boolean[] result = {false};
        Flux<IpLimitApiDTO> blackList = resourceLocator.getIpBlacks();
        blackList.filter(api -> pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty())
                .filter(api -> matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin))
                .subscribe(r -> result[0] = true);
        return result[0];

    }

    /**
     * 白名单验证
     *
     * @return [hasWhiteList, allow]
     */
    public Boolean[] matchIpOrOriginWhiteList(String requestPath, String ipAddress, String origin) {
        final Boolean[] result = {false, false};
        boolean hasWhiteList = false;
        boolean allow = false;
        Flux<IpLimitApiDTO> whiteList = resourceLocator.getIpWhites();
        whiteList.filter(api -> pathMatch.match(api.getPath(), requestPath) && api.getIpAddressSet() != null && !api.getIpAddressSet().isEmpty())
                .subscribe(api -> {
                    result[0] = true;
                    result[1] = matchIpOrOrigin(api.getIpAddressSet(), ipAddress, origin);
                });
        return result;
    }

    /**
     * 匹配IP或域名
     */
    public boolean matchIpOrOrigin(Set<String> values, String ipAddress, String origin) {
        ReactiveIpAddressMatcher ipAddressMatcher;
        for (String value : values) {
            if (StringUtils.matchIp(value)) {
                ipAddressMatcher = new ReactiveIpAddressMatcher(value);
                if (ipAddressMatcher.matches(ipAddress)) {
                    return true;
                }
            } else {
                if (StringUtils.matchDomain(value) && StringUtils.isNotBlank(origin) && origin.contains(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}