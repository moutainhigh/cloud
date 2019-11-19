package com.smart4y.cloud.gateway.infrastructure.security;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.smart4y.cloud.core.domain.OpenAuthority;
import com.smart4y.cloud.core.infrastructure.constants.CommonConstants;
import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import com.smart4y.cloud.core.infrastructure.toolkit.base.StringHelper;
import com.smart4y.cloud.core.interfaces.AuthorityResourceDTO;
import com.smart4y.cloud.gateway.infrastructure.locator.ResourceLocator;
import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import com.smart4y.cloud.gateway.infrastructure.toolkit.ReactiveIpAddressMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
        boolean permit = permitAll.stream()
                .anyMatch(r -> pathMatch.match(r, requestPath));
        if (permit) {
            return true;
        }
        // 动态权限列表
        return resourceLocator.getAuthorityResources().stream()
                .filter(res -> StringUtils.isNotBlank(res.getPath()))
                .anyMatch(res -> {
                    // 无需认证，返回true
                    boolean isAuth = res.getIsAuth() != null && res.getIsAuth() == 1;
                    return pathMatch.match(res.getPath(), requestPath) && !isAuth;
                });
    }

    /**
     * 获取资源状态
     */
    public AuthorityResourceDTO getResource(String requestPath) {
        // 动态权限列表
        return resourceLocator.getAuthorityResources()
                .stream()
                .filter(r -> StringUtils.isNotBlank(r.getPath()))
                .filter(r -> !"/**".equals(r.getPath()))
                .filter(r -> pathMatch.match(r.getPath(), requestPath))
                .filter(r -> !permitAll(r.getPath()))
                .findFirst().orElse(null);
    }

    /**
     * 忽略鉴权
     */
    private boolean authorityIgnores(String requestPath) {
        return authorityIgnores.stream()
                .anyMatch(r -> pathMatch.match(r, requestPath));
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
        AtomicReference<Collection<ConfigAttribute>> attributes = new AtomicReference<>();
        resourceLocator.getConfigAttributes().keySet().stream()
                .filter(r -> !"/**".equals(r))
                .filter(r -> pathMatch.match(r, requestPath))
                .findFirst().ifPresent(r -> {
            attributes.set(resourceLocator.getConfigAttributes().get(r));
        });
        if (attributes.get() != null) {
            return attributes.get();
        }
        return SecurityConfig.createList("AUTHORITIES_REQUIRED");
    }

    /**
     * IP黑名单验证
     */
    public boolean matchIpOrOriginBlacklist(String requestPath, String ipAddress, String origin) {
        return resourceLocator.getIpBlacks().stream()
                .filter(r -> StringUtils.isNotEmpty(r.getPath()))
                .filter(r -> r.getIpAddressSet() != null && !r.getIpAddressSet().isEmpty())
                .filter(r -> pathMatch.match(r.getPath(), requestPath))
                .anyMatch(r -> matchIpOrOrigin(r.getIpAddressSet(), ipAddress, origin));

    }

    /**
     * 白名单验证
     *
     * @return [hasWhiteList, allow]
     */
    public Boolean[] matchIpOrOriginWhiteList(String requestPath, String ipAddress, String origin) {
        final Boolean[] result = {false, false};
        resourceLocator.getIpWhites().stream()
                .filter(r -> StringUtils.isNotEmpty(r.getPath()))
                .filter(r -> r.getIpAddressSet() != null && !r.getIpAddressSet().isEmpty())
                .filter(r -> pathMatch.match(r.getPath(), requestPath))
                .findFirst().ifPresent(r -> {
            result[0] = true;
            result[1] = matchIpOrOrigin(r.getIpAddressSet(), ipAddress, origin);
        });
        return result;
    }

    /**
     * 匹配IP或域名
     */
    public boolean matchIpOrOrigin(Set<String> values, String ipAddress, String origin) {
        ReactiveIpAddressMatcher ipAddressMatcher;
        for (String value : values) {
            if (StringHelper.matchIp(value)) {
                ipAddressMatcher = new ReactiveIpAddressMatcher(value);
                if (ipAddressMatcher.matches(ipAddress)) {
                    return true;
                }
            } else {
                if (StringHelper.matchDomain(value) && StringHelper.isNotBlank(origin) && origin.contains(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}