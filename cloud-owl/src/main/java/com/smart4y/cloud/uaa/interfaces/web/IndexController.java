package com.smart4y.cloud.uaa.interfaces.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Youtao
 *         Created by youtao on 2019-09-05.
 */
@Controller
public class IndexController {

    /**
     * 欢迎页
     */
    @GetMapping("/")
    @ApiOperation(value = "欢迎页", notes = "欢迎页")
    public String welcome() {
        return "welcome";
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    @ApiOperation(value = "登录页", notes = "登录页")
    public String login(HttpServletRequest request) {
        return "login";
    }
}