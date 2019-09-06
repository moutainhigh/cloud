package com.smart4y.cloud.core.infrastructure.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Loger
 */
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        XssServletRequestWrapper xssRequestWrapper = new XssServletRequestWrapper(req);
        chain.doFilter(xssRequestWrapper, response);
    }

    @Override
    public void destroy() {

    }


}
