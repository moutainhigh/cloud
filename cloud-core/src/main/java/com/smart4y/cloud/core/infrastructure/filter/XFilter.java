package com.smart4y.cloud.core.infrastructure.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 参数去除空格过滤器
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class XFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        XServletRequestWrapper xssRequestWrapper = new XServletRequestWrapper(req);
        chain.doFilter(xssRequestWrapper, response);
    }

    @Override
    public void destroy() {
    }
}