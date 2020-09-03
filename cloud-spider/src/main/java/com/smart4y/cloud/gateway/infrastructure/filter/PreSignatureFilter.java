package com.smart4y.cloud.gateway.infrastructure.filter;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.google.common.collect.Maps;
import com.smart4y.cloud.core.constant.CommonConstants;
import com.smart4y.cloud.core.exception.OpenSignatureException;
import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.toolkit.secret.SignatureUtils;
import com.smart4y.cloud.gateway.domain.GatewayContext;
import com.smart4y.cloud.gateway.infrastructure.exception.JsonSignatureDeniedHandler;
import com.smart4y.cloud.gateway.infrastructure.feign.AppDTO;
import com.smart4y.cloud.gateway.infrastructure.feign.BaseAppFeign;
import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数字验签前置 过滤器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class PreSignatureFilter implements WebFilter {

    private static final AntPathMatcher pathMatch = new AntPathMatcher();
    private final JsonSignatureDeniedHandler signatureDeniedHandler;
    private final BaseAppFeign baseAppFeign;
    private final ApiProperties apiProperties;
    private final Set<String> signIgnores = new ConcurrentHashSet<>();

    public PreSignatureFilter(BaseAppFeign baseAppFeign, ApiProperties apiProperties, JsonSignatureDeniedHandler signatureDeniedHandler) {
        this.apiProperties = apiProperties;
        this.baseAppFeign = baseAppFeign;
        this.signatureDeniedHandler = signatureDeniedHandler;
        // 默认忽略签名
        signIgnores.add("/");
        signIgnores.add("/error");
        signIgnores.add("/favicon.ico");
        if (apiProperties != null) {
            if (apiProperties.getSignIgnores() != null) {
                signIgnores.addAll(apiProperties.getSignIgnores());
            }
            if (apiProperties.isApiDebug()) {
                signIgnores.add("/**/v2/api-docs/**");
                signIgnores.add("/**/swagger-resources/**");
                signIgnores.add("/webjars/**");
                signIgnores.add("/doc.html");
                signIgnores.add("/swagger-ui.html");
            }
        }
    }

    protected static List<String> getIgnoreMatchers(String... antPatterns) {
        List<String> matchers = new CopyOnWriteArrayList<>();
        Collections.addAll(matchers, antPatterns);
        return matchers;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        List<String> traceId = exchange.getRequest().getHeaders().get(FeignRequestInterceptor.X_REQUEST_ID);
        String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                this.getClass().getSimpleName(), traceId, (exchange.getRequest().getPath() + exchange.getRequest().getMethodValue()));
        log.info(format);

        String requestPath = request.getURI().getPath();
        if (apiProperties.isCheckSign() && !notSign(requestPath)) {
            try {
                Map<String, String> params = Maps.newHashMap();
                GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
                // 排除文件上传
                if (gatewayContext != null) {
                    params = gatewayContext.getAllRequestData().toSingleValueMap();
                }
                // 验证请求参数
                SignatureUtils.validateParams(params);
                // 开始验证签名
                if (baseAppFeign != null) {
                    String appId = params.get(CommonConstants.SIGN_APP_ID_KEY);
                    // 获取客户端信息
                    ResultMessage<AppDTO> result = baseAppFeign.getApp(appId);
                    AppDTO app = result.getData();
                    if (app == null || app.getAppId() == null) {
                        return signatureDeniedHandler.handle(exchange, new OpenSignatureException("appId无效"));
                    }
                    // 服务器验证签名结果
                    if (!SignatureUtils.validateSign(params, app.getSecretKey())) {
                        return signatureDeniedHandler.handle(exchange, new OpenSignatureException("签名验证失败!"));
                    }
                }
            } catch (Exception ex) {
                return signatureDeniedHandler.handle(exchange, new OpenSignatureException(ex.getMessage()));
            }
        }
        return chain.filter(exchange);
    }

    private boolean notSign(String requestPath) {
        if (apiProperties.getSignIgnores() == null) {
            return false;
        }
        for (String path : signIgnores) {
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }
}