package com.smart4y.cloud.core.configuration;

import com.smart4y.cloud.core.exception.OpenException;
import com.smart4y.cloud.core.message.enums.MessageType;
import com.smart4y.cloud.core.properties.OpenSwaggerProperties;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
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

    private final OpenSwaggerProperties openSwaggerProperties;

    public SwaggerAutoConfiguration(OpenSwaggerProperties openSwaggerProperties) {
        this.openSwaggerProperties = openSwaggerProperties;
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
}