package com.smart4y.cloud.core.configuration;

import com.google.common.collect.Lists;
import com.smart4y.cloud.core.exception.OpenException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.properties.OpenSwaggerProperties;
import com.smart4y.cloud.core.toolkit.base.DateHelper;
import com.smart4y.cloud.core.toolkit.random.RandomValueUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Swagger文档生成配置
 *
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@EnableConfigurationProperties({OpenSwaggerProperties.class})
@ConditionalOnProperty(prefix = "cloud.swagger2", name = "enabled", havingValue = "true")
@Import({Swagger2DocumentationConfiguration.class})
public class SwaggerAutoConfiguration {

    private static final String SCOPE_PREFIX = "scope.";
    private final OpenSwaggerProperties openSwaggerProperties;
    private final Locale locale = LocaleContextHolder.getLocale();
    private final MessageSource messageSource;

    public SwaggerAutoConfiguration(OpenSwaggerProperties openSwaggerProperties, MessageSource messageSource) {
        this.openSwaggerProperties = openSwaggerProperties;
        this.messageSource = messageSource;
        log.info("SwaggerProperties [{}]", openSwaggerProperties);
    }

    @Bean
    public Docket createRestApi() {
        // 添加全局响应状态码
        List<ResponseMessage> responseMessages = new ArrayList<>();
        ResponseMessage ok = new ResponseMessageBuilder()
                .code(Integer.parseInt(MessageType.OK.getRtnCode()))
                .message(OpenException.getBundleMessageText(MessageType.OK))
                .responseModel(new ModelRef(OpenException.getBundleMessageText(MessageType.OK)))
                .build();
        responseMessages.add(ok);

        return new Docket(DocumentationType.SWAGGER_2)

                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.PATCH, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)

                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title(openSwaggerProperties.getTitle())
                .description(openSwaggerProperties.getDescription())
                .version("1.0")
                .build();
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        SecurityConfiguration realm = new SecurityConfiguration(openSwaggerProperties.getClientId(),
                openSwaggerProperties.getClientSecret(),
                "realm", openSwaggerProperties.getClientId(),
                "", ApiKeyVehicle.HEADER, "", ",");
        return realm;
    }

    @Bean
    public List<GrantType> grantTypes() {
        List<GrantType> grantTypes = new ArrayList<>();
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(
                openSwaggerProperties.getUserAuthorizationUri(),
                openSwaggerProperties.getClientId(), openSwaggerProperties.getClientSecret());
        TokenEndpoint tokenEndpoint = new TokenEndpoint(
                openSwaggerProperties.getAccessTokenUri(), "access_token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
    }

    @Bean
    public UiConfiguration uiConfig() {
        UiConfiguration uiConfiguration = new UiConfiguration(null, "list", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
        return uiConfiguration;
    }

    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     */
    public SecurityScheme apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }


    /**
     * 构建全局参数
     * 这里主要针对网关服务外部访问数字验签所需参数
     * 只在网关服务开启{cloud.resource-server.enabled-validate-sign=true}时生效.
     * 未开启,可以不填写
     */
    private List<Parameter> parameters() {
        ParameterBuilder builder = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        builder.name("Authorization").description("公共参数:认证token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false);
        pars.add(builder.build());
        builder.name("clientId").description("公共参数:客户端Id")
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("nonce").description("公共参数:随机字符串")
                .defaultValue(RandomValueUtils.uuid())
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("timestamp").description("公共参数:请求的时间,格式:yyyyMMddHHmmss")
                .defaultValue(DateHelper.getCurrentTimestampStr())
                .modelRef(new ModelRef("string")).parameterType("form")
                .required(false);
        pars.add(builder.build());
        builder.name("signType").description("公共参数:签名方式:MD5(默认)、SHA256.")
                .modelRef(new ModelRef("string")).parameterType("form")
                .allowableValues(new AllowableListValues(Lists.newArrayList("MD5", "SHA256"), "string"))
                .required(true);
        pars.add(builder.build());
        builder = new ParameterBuilder();
        builder.name("sign").description("公共参数:数字签名")
                .modelRef(new ModelRef("string")).parameterType("form")
                .defaultValue("")
                .required(false);
        pars.add(builder.build());
        return pars;
    }

    private List<AuthorizationScope> scopes() {
        List<String> scopes = Lists.newArrayList();
        List list = Lists.newArrayList();
        if (openSwaggerProperties.getScope() != null) {
            scopes.addAll(Lists.newArrayList(openSwaggerProperties.getScope().split(",")));
        }
        scopes.forEach(s -> list.add(new AuthorizationScope(s, messageSource.getMessage(SCOPE_PREFIX + s, null, s, locale))));
        return list;
    }
}