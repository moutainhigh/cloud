package com.smart4y.cloud.gateway.infrastructure.feign;

import com.smart4y.cloud.core.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.message.ResultMessage;
import com.smart4y.cloud.core.constant.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 系统权限信息服务
 *
 * @author Youtao
 *         Created by youtao on 2019-09-06.
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVER)
public interface BaseAuthorityFeign {

    /**
     * 获取所有访问权限列表
     */
    @GetMapping("/authority/access")
    ResultMessage<List<AuthorityResourceDTO>> findAuthorityResource();

    /**
     * 获取菜单权限列表
     */
    @GetMapping("/authority/menu")
    ResultMessage<List<AuthorityMenuDTO>> findAuthorityMenu();
}