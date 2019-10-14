package com.smart4y.cloud.gateway.interfaces.web;

import com.smart4y.cloud.gateway.infrastructure.properties.ApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-08.
 */
@Slf4j
@Controller
public class IndexController {

    @Value("${spring.application.name}")
    private String serviceId;

    private final ApiProperties apiProperties;

    @Autowired
    public IndexController(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @GetMapping("/")
    public String index() {
        if (apiProperties.isApiDebug()) {
            return "redirect:doc.html";
        }
        return "index";
    }
}