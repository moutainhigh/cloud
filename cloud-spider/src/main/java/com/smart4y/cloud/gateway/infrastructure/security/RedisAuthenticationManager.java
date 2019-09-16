package com.smart4y.cloud.gateway.infrastructure.security;

import com.smart4y.cloud.core.infrastructure.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * Redis缓存认证 管理类
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Slf4j
public class RedisAuthenticationManager implements ReactiveAuthenticationManager {

    private TokenStore tokenStore;

    public RedisAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((token -> {
                    OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(token);
                    if (oAuth2Authentication == null) {
                        return Mono.error(new InvalidTokenException(ErrorCode.INVALID_TOKEN.getMessage()));
                    } else {
                        return Mono.just(oAuth2Authentication);
                    }
                }))
                .cast(Authentication.class);
    }

    public TokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
}