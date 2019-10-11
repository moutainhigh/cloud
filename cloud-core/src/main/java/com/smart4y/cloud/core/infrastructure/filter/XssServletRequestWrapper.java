package com.smart4y.cloud.core.infrastructure.filter;

import com.smart4y.cloud.core.infrastructure.toolkit.StringUtil;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * XSS 过滤
 * body 缓存
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class XssServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;
    private final byte[] body;

    public XssServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), baos);
        this.body = baos.toByteArray();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public String getParameter(String name) {
        name = StringUtil.stripXss(name);
        String value = request.getParameter(name);
        if (!StringUtil.isEmpty(value)) {
            value = StringUtil.stripXss(value).trim();
        }
        return value;
    }

    @Override
    public String getHeader(String name) {
        name = StringUtil.stripXss(name);
        String value = super.getHeader(name);
        if (StringUtil.isNotBlank(value)) {
            value = StringUtil.stripXss(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        name = StringUtil.stripXss(name);
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringUtil.stripXss(value).trim();
        }
        return parameterValues;
    }
}