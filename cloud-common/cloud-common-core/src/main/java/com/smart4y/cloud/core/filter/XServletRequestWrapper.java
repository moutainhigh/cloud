package com.smart4y.cloud.core.filter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.*;

/**
 * 参数去除空格
 * body 缓存
 * headers 缓存
 *
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class XServletRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;
    private final byte[] body;
    private Map<String, String> customHeaders;

    public XServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), byteArrayOutputStream);
        this.body = byteArrayOutputStream.toByteArray();
        this.customHeaders = new HashMap<>();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() {
                return byteArrayInputStream.read();
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
        String value = request.getParameter(name);
        if (!StringUtils.isEmpty(value)) {
            value = StringUtils.trim(value).trim();
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringUtils.trim(value).trim();
        }
        return parameterValues;
    }

    @Override
    public String getHeader(String name) {
        String value = this.customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            set.add(name);
        }
        return Collections.enumeration(set);
    }
}