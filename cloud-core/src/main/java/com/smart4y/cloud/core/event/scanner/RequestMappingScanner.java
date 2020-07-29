package com.smart4y.cloud.core.event.scanner;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.constant.BaseConstants;
import com.smart4y.cloud.core.constant.QueueConstants;
import com.smart4y.cloud.core.event.ResourceScannedEvent;
import com.smart4y.cloud.core.properties.OpenScanProperties;
import com.smart4y.cloud.core.toolkit.base.StringHelper;
import com.smart4y.cloud.core.toolkit.reflection.ReflectionUtils;
import com.smart4y.cloud.core.toolkit.secret.EncryptUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 自定义 资源注解扫描事件处理器
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
public class RequestMappingScanner implements ApplicationListener<ApplicationReadyEvent> {

    private static final AntPathMatcher pathMatch = new AntPathMatcher();
    private final OpenScanProperties scanProperties;
    private AmqpTemplate amqpTemplate;
    private static boolean scanned = false;

    public RequestMappingScanner(AmqpTemplate amqpTemplate, OpenScanProperties scanProperties) {
        this.amqpTemplate = amqpTemplate;
        this.scanProperties = scanProperties;
    }

    /**
     * 初始化方法
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        if (scanned || scanProperties == null || !scanProperties.isRegisterRequestMapping()) {
            return;
        }
        scanned = true;
        Environment env = applicationContext.getEnvironment();
        // 服务名称
        String serviceId = env.getRequiredProperty("spring.application.name");
        // 所有接口映射
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        // 获取忽略鉴权资源集
        List<RequestMatcher> permitAll = getPermitAllUrls(applicationContext);

        List<String> apiCodes = new ArrayList<>();
        List<ResourceScannedEvent.Mapping> list = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            if (method.getMethodAnnotation(ApiIgnore.class) != null) {
                // 忽略的接口不扫描
                continue;
            }
            Set<MediaType> mediaTypeSet = info.getProducesCondition().getProducibleMediaTypes();
            for (MethodParameter params : method.getMethodParameters()) {
                if (params.hasParameterAnnotation(RequestBody.class)) {
                    mediaTypeSet.add(MediaType.APPLICATION_JSON);
                    break;
                }
            }
            String mediaTypes = getMediaTypes(mediaTypeSet);
            // 请求类型
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            // 请求方法类型GET,POST...
            String methods = getMethods(methodsCondition.getMethods());
            // 请求路径
            PatternsRequestCondition p = info.getPatternsCondition();
            String urls = getUrls(p.getPatterns());
            // 类名
            String className = method.getMethod().getDeclaringClass().getName();
            // 方法名
            String methodName = method.getMethod().getName();
            // 方法全类限定名
            // String fullName = className + "." + methodName;
            // md5
            String md5 = EncryptUtils.md5Hex(serviceId + urls + methods);
            if (apiCodes.contains(md5)) {
                continue;
            } else {
                apiCodes.add(md5);
            }

            String name = "";
            String desc = "";
            // 是否需要安全认证 默认:1-是 0-否
            int isAuth = BaseConstants.ENABLED;
            // 匹配项目中.permitAll()配置
            for (String url : p.getPatterns()) {
                for (RequestMatcher requestMatcher : permitAll) {
                    if (requestMatcher instanceof AntPathRequestMatcher) {
                        AntPathRequestMatcher pathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
                        if (pathMatch.match(pathRequestMatcher.getPattern(), url)) {
                            // 忽略验证
                            isAuth = BaseConstants.DISABLED;
                        }
                    }
                }
            }

            ApiOperation apiOperation = method.getMethodAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                name = apiOperation.value();
                desc = apiOperation.notes();
            }
            name = StringHelper.isBlank(name) ? methodName : name;

            ResourceScannedEvent.Mapping mappings = new ResourceScannedEvent.Mapping()
                    .setApiName(name)
                    .setApiCode(md5)
                    .setApiDesc(desc)
                    .setPath(urls)
                    .setClassName(className)
                    .setMethodName(methodName)
                    .setMd5(md5)
                    .setRequestMethod(methods)
                    .setServiceId(serviceId)
                    .setContentType(mediaTypes)
                    .setIsAuth(isAuth);
            list.add(mappings);
        }

        ResourceScannedEvent scannedEvent = new ResourceScannedEvent()
                .setApplication(serviceId)
                .setMappings(list);
        log.info("ApplicationReadyEvent: [{}]", serviceId);

        amqpTemplate = applicationContext.getBean(RabbitTemplate.class);
        CompletableFuture.runAsync(() -> {
            try {
                amqpTemplate.convertAndSend(QueueConstants.QUEUE_SCAN_API_RESOURCE, scannedEvent);
            } catch (Exception e) {
                log.error("发送失败：{}", e.getLocalizedMessage(), e);
            }
        });
    }

    /**
     * 获取忽略鉴权资源集
     */
    private List<RequestMatcher> getPermitAllUrls(ConfigurableApplicationContext applicationContext) {
        List<RequestMatcher> permitAll = Lists.newArrayList();
        try {
            // 获取所有安全配置适配器
            Map<String, WebSecurityConfigurerAdapter> securityConfigurerAdapterMap = applicationContext.getBeansOfType(WebSecurityConfigurerAdapter.class);
            for (Map.Entry<String, WebSecurityConfigurerAdapter> stringWebSecurityConfigurerAdapterEntry : securityConfigurerAdapterMap.entrySet()) {
                WebSecurityConfigurerAdapter configurer = stringWebSecurityConfigurerAdapterEntry.getValue();
                HttpSecurity httpSecurity = (HttpSecurity) ReflectionUtils.getFieldValue(configurer, "http");
                if (null != httpSecurity) {
                    FilterSecurityInterceptor filterSecurityInterceptor = httpSecurity.getSharedObject(FilterSecurityInterceptor.class);
                    FilterInvocationSecurityMetadataSource metadataSource = filterSecurityInterceptor.getSecurityMetadataSource();
                    Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = (Map) ReflectionUtils.getFieldValue(metadataSource, "requestMap");
                    if (null != requestMap) {
                        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> match : requestMap.entrySet()) {
                            if (match.getValue().toString().contains("permitAll")) {
                                permitAll.add(match.getKey());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("error：{}", e.getLocalizedMessage(), e);
        }
        return permitAll;
    }

    private String getMediaTypes(Set<MediaType> mediaTypes) {
        StringBuilder sbf = new StringBuilder();
        for (MediaType mediaType : mediaTypes) {
            sbf.append(mediaType.toString()).append(",");
        }
        if (mediaTypes.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getMethods(Set<RequestMethod> requestMethods) {
        StringBuilder sbf = new StringBuilder();
        for (RequestMethod requestMethod : requestMethods) {
            sbf.append(requestMethod.toString()).append(",");
        }
        if (requestMethods.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getUrls(Set<String> urls) {
        StringBuilder sbf = new StringBuilder();
        for (String url : urls) {
            sbf.append(url).append(",");
        }
        if (urls.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }
}