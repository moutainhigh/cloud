package com.smart4y.cloud.gateway.interfaces.web;

import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-08.
 */
@Controller
public class IndexController {

    @Value("${spring.application.name}")
    private String serviceId;

    private final ApiProperties apiProperties;
    private final RouteDefinitionLocator routeDefinitionLocator;

    @Autowired
    public IndexController(ApiProperties apiProperties, RouteDefinitionLocator routeDefinitionLocator) {
        this.apiProperties = apiProperties;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @GetMapping("/")
    public String index() {
        if (apiProperties.isApiDebug()) {
            return "redirect:doc.html";
        }
        return "index";
    }
}