package com.smart4y.cloud.core.security;

import com.smart4y.cloud.core.toolkit.base.BeanConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义认证用户信息转换器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class OpenUserConverter extends DefaultUserAuthenticationConverter {

    public OpenUserConverter() {
    }

    /**
     * 转换为自定义信息
     */
    private Object converter(Map<String, ?> map) {
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (USERNAME.equals(key)) {
                if (value instanceof Map) {
                    Map map1 = (Map) value;
                    params.putAll(map1);
                } else if (map.get(key) instanceof OpenUserDetails) {
                    return map.get(key);
                } else {
                    params.put(key, map.get(key));
                }
            } else {
                params.put(key, value);
            }
        }
        OpenUserDetails auth = BeanConvertUtils.mapToObject(params, OpenUserDetails.class);
        if (params.get(USERNAME) != null) {
            auth.setUsername(params.get(USERNAME).toString());
        }
        if (params.get(OpenSecurityConstants.OPEN_ID) != null) {
            auth.setUserId(Long.parseLong(params.get(OpenSecurityConstants.OPEN_ID).toString()));
        }
        if (params.get(OpenSecurityConstants.DOMAIN) != null) {
            auth.setDomain(params.get(OpenSecurityConstants.DOMAIN).toString());
        }
        auth.setClientId(params.get(AccessTokenConverter.CLIENT_ID).toString());
        auth.setAuthorities(getAuthorities(map));
        return auth;
    }

    /**
     * 转换用户
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getPrincipal());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * 读取认证信息
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Object principal = converter(map);
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            if (principal != null) {
                OpenUserDetails user = (OpenUserDetails) principal;
                authorities = user.getAuthorities();
            }
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }

    /**
     * 获取权限
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return AuthorityUtils.NO_AUTHORITIES;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}