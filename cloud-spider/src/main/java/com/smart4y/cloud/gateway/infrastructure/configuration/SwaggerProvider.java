package com.smart4y.cloud.gateway.infrastructure.configuration;

import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
public class SwaggerProvider implements SwaggerResourcesProvider {

    private static final String API_URI = "/v2/api-docs";
    private final RouteDefinitionLocator routeDefinitionLocator;

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
                            .filter(predicateDefinition -> !predicateDefinition.getArgs().containsKey("_rateLimit"))
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
}