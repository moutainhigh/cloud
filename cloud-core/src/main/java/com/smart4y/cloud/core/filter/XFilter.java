package com.smart4y.cloud.core.filter;

import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 参数去除空格过滤器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class XFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String xRequestId = req.getHeader(FeignRequestInterceptor.X_REQUEST_ID);
        MDC.put(FeignRequestInterceptor.X_REQUEST_ID, xRequestId);

        XServletRequestWrapper xssRequestWrapper = new XServletRequestWrapper(req);
        chain.doFilter(xssRequestWrapper, response);
    }

    @Override
    public void destroy() {
    }
}