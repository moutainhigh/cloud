package com.smart4y.cloud.spider.infrastructure.configuration;

import com.smart4y.cloud.core.properties.OpenCommonProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Youtao
 * Created by youtao on 2019-09-05.
 */
@Slf4j
@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";
    private final RouteDefinitionLocator routeDefinitionLocator;

    @Autowired
    public SwaggerProvider(RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        // 结合配置的route-路径(Path)，和route过滤，只获取有效的route节点
        Flux<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions();
        routeDefinitions
                .filter(routeDefinition -> routeDefinition.getUri().toString().contains("lb://"))
                .subscribe(routeDefinition -> {
                    List<PredicateDefinition> predicates = routeDefinition.getPredicates();
                    predicates.stream()
                            .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
//                            .filter(predicateDefinition -> !predicateDefinition.getArgs().containsKey("_rateLimit"))
                            .forEach(predicateDefinition -> {
                                SwaggerResource swaggerResource = swaggerResource(predicateDefinition.getArgs().get("name"), predicateDefinition.getArgs().get("pattern").replace("/**", API_URI));
                                resources.add(swaggerResource);
                            });
                });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

    /**
     * Swagger相关端点
     */
    @RestController
    public static class SwaggerEndpoint {

        private final SwaggerResourcesProvider swaggerResourcesProvider;
        private final OpenCommonProperties commonProperties;

        @Autowired
        public SwaggerEndpoint(SwaggerResourcesProvider swaggerResourcesProvider, OpenCommonProperties commonProperties) {
            this.swaggerResourcesProvider = swaggerResourcesProvider;
            this.commonProperties = commonProperties;
        }

        @GetMapping("/swagger-resources")
        public Mono<ResponseEntity<List<SwaggerResource>>> swaggerResources() {
            return Mono.just(
                    new ResponseEntity<>(
                            swaggerResourcesProvider.get(),
                            HttpStatus.OK));
        }

        @GetMapping("/swagger-resources/configuration/security")
        public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
            return Mono.just(
                    new ResponseEntity<>(
                            Optional.ofNullable(security()).orElse(SecurityConfigurationBuilder.builder().build()),
                            HttpStatus.OK));
        }

        @GetMapping("/swagger-resources/configuration/ui")
        public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
            return Mono.just(
                    new ResponseEntity<>(
                            Optional.ofNullable(uiConfig())
                                    .orElse(UiConfigurationBuilder.builder().build()),
                            HttpStatus.OK));
        }

        public SecurityConfiguration security() {
            return SecurityConfigurationBuilder.builder()
                    .clientId(commonProperties.getClientId())
                    .clientSecret(commonProperties.getClientSecret())
                    .realm("realm")
                    .appName(commonProperties.getClientId())
                    .scopeSeparator(",")
                    .build();
        }

        public UiConfiguration uiConfig() {
            return UiConfigurationBuilder.builder()
                    .validatorUrl(null)
                    .docExpansion(DocExpansion.LIST)
                    .operationsSorter(OperationsSorter.ALPHA)
                    .defaultModelRendering(ModelRendering.MODEL)
                    .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                    .build();
        }
    }

    /**
     * Swagger `/v2/api-docs`请求头处理过滤器
     */
    @Component
    public static class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

        private static final String HEADER_NAME = "X-Forwarded-Prefix";
        private static final String URI = "/v2/api-docs";

        @Override
        public GatewayFilter apply(Object config) {
            return (exchange, chain) -> {
                ServerHttpRequest request = exchange.getRequest();
                log.info(">>>>>>>>>>> {}", request.getURI());
                String path = request.getURI().getPath();
                if (!StringUtils.endsWithIgnoreCase(path, URI)) {
                    return chain.filter(exchange);
                }

                String basePath = path.substring(0, path.lastIndexOf(URI));
                ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                return chain.filter(newExchange);
            };
        }
    }
}