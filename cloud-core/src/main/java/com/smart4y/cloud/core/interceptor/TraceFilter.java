package com.smart4y.cloud.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下游服务拦截器
 * <p>
 * 只有 Servlet 方式会生效
 * </p>
 *
 * @author Youtao on 2020/8/25 14:15
 */
@Slf4j
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = request.getHeader(FeignRequestInterceptor.X_REQUEST_ID);
            if (StringUtils.isNotBlank(traceId)) {
                MDC.put(FeignRequestInterceptor.X_REQUEST_ID, traceId);
            }

            String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                    this.getClass().getSimpleName(), traceId, (request.getRequestURI() + request.getMethod()));
            System.out.println(format);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}