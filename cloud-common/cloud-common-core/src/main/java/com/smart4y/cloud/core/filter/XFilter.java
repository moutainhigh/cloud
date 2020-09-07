package com.smart4y.cloud.core.filter;

import com.smart4y.cloud.core.interceptor.FeignRequestInterceptor;
import lombok.extern.slf4j.Slf4j;

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

        String traceId = req.getHeader(FeignRequestInterceptor.X_REQUEST_ID);
        String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                this.getClass().getSimpleName(), traceId, (req.getRequestURI() + req.getMethod()));
        System.out.println(format);

        XServletRequestWrapper xssRequestWrapper = new XServletRequestWrapper(req);
        chain.doFilter(xssRequestWrapper, response);
    }

    @Override
    public void destroy() {
    }
}