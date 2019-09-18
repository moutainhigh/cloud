package com.smart4y.cloud.gateway.interfaces.web;

import com.smart4y.cloud.core.infrastructure.properties.OpenCommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@RestController
@RequestMapping(value = "/swagger-resources")
public class SwaggerController {

    private final SwaggerResourcesProvider swaggerResourcesProvider;
    private final OpenCommonProperties commonProperties;

    @Autowired
    public SwaggerController(SwaggerResourcesProvider swaggerResourcesProvider, OpenCommonProperties commonProperties) {
        this.swaggerResourcesProvider = swaggerResourcesProvider;
        this.commonProperties = commonProperties;
    }

    /**
     * swagger安全配置
     */
    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(commonProperties.getClientId())
                .clientSecret(commonProperties.getClientSecret())
                .realm("realm")
                .appName(commonProperties.getClientId())
                .scopeSeparator(",")
                .build();
        //return new SecurityConfiguration(commonProperties.getClientId(),
        //        commonProperties.getClientSecret(),
        //        "realm", commonProperties.getClientId(),
        //        "", ApiKeyVehicle.HEADER, "", ",");
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .validatorUrl(null)
                .docExpansion(DocExpansion.LIST)
                .operationsSorter(OperationsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .build();
        //return new UiConfiguration(null, "list", "alpha", "schema",
        //        UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }

    @GetMapping
    public Mono<ResponseEntity> swaggerResources() {
        List<SwaggerResource> resources = swaggerResourcesProvider.get();
        return Mono.just((new ResponseEntity<>(resources, HttpStatus.OK)));
    }

    @GetMapping("/configuration/security")
    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
        SecurityConfiguration securityConfiguration = security();
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(securityConfiguration).orElse(null), HttpStatus.OK));
    }

    @GetMapping("/configuration/ui")
    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
        UiConfiguration uiConfiguration = uiConfig();
        UiConfiguration uiConfig = UiConfigurationBuilder.builder().validatorUrl("/").build();
        return Mono.just(new ResponseEntity<>(
                Optional.ofNullable(uiConfiguration).orElse(uiConfig), HttpStatus.OK));
    }
}