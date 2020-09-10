package com.smart4y.cloud.core.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微服务之间feign调用请求头丢失的问题
 * 加入微服务之间传递的唯一标识，便于追踪。
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * 微服务之间传递的唯一标识
     */
    public static final String X_REQUEST_ID = "X-Request-Id";
    public static final String X_REQUEST_TIME = "X-Request-Time";

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = Objects.requireNonNull(attributes.getRequest());
            Map<String, String> headers = getHeaders(request);

            // 传递所有请求头,防止部分丢失
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }

            // 微服务之间传递的唯一标识
            String traceId = request.getHeader(X_REQUEST_ID);
            MDC.put(X_REQUEST_ID, traceId);
            template.header(X_REQUEST_ID, traceId);

            String format = String.format(">>>>> >>>>> >>>>> %s, traceId=%s, path=%s",
                    this.getClass().getSimpleName(), traceId, (request.getRequestURI() + request.getMethod()));
            System.out.println(format);

            log.info(template.toString());
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }
}