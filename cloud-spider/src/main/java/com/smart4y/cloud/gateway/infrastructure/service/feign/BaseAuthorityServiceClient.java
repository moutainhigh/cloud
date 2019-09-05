package com.smart4y.cloud.gateway.infrastructure.service.feign;

import com.smart4y.cloud.core.AuthorityMenu;
import com.smart4y.cloud.core.AuthorityResource;
import com.smart4y.cloud.core.ResultBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

/**
 * 权限控制API接口
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:49
 * @description:
 */
@Component
public class BaseAuthorityServiceClient {

    /**
     * 获取所有访问权限列表
     */
    @GetMapping("/authority/access")
    public ResultBody<List<AuthorityResource>> findAuthorityResource() {
        // TODO
        List<AuthorityResource> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }

    /**
     * 获取菜单权限列表
     */
    @GetMapping("/authority/menu")
    public ResultBody<List<AuthorityMenu>> findAuthorityMenu() {
        // TODO
        List<AuthorityMenu> resources = Collections.emptyList();
        return new ResultBody<>().data(resources);
    }
}