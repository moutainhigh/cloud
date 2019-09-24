package com.smart4y.cloud.base.application.impl;

import com.smart4y.cloud.base.application.AuthorityService;
import com.smart4y.cloud.base.domain.model.BaseAction;
import com.smart4y.cloud.base.domain.model.BaseMenu;
import com.smart4y.cloud.base.domain.service.BaseAuthorityDomainService;
import com.smart4y.cloud.core.application.annotation.ApplicationService;
import com.smart4y.cloud.core.application.dto.AuthorityMenuDTO;
import com.smart4y.cloud.core.application.dto.AuthorityResourceDTO;
import com.smart4y.cloud.core.infrastructure.security.http.OpenRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Youtao
 *         Created by youtao on 2019/9/17.
 */
@Slf4j
@ApplicationService
public class AuthorityServiceImpl implements AuthorityService {

    private final OpenRestTemplate openRestTemplate;
    private final BaseAuthorityDomainService baseAuthorityDomainService;

    @Autowired
    public AuthorityServiceImpl(BaseAuthorityDomainService baseAuthorityDomainService, OpenRestTemplate openRestTemplate) {
        this.baseAuthorityDomainService = baseAuthorityDomainService;
        this.openRestTemplate = openRestTemplate;
    }

    @Override
    public List<AuthorityResourceDTO> getAuthorityResources() {
        return baseAuthorityDomainService.getAuthorityResources();
    }

    @Override
    public List<AuthorityMenuDTO> getMenuAuthoritiesAll() {
        return baseAuthorityDomainService.getMenuAuthoritiesAll();
    }

    @Override
    public List<BaseMenu> getMenus() {
        return baseAuthorityDomainService.getMenus();
    }

    @Override
    public List<BaseAction> getMenuActionsByMenuId(long menuId) {
        return baseAuthorityDomainService.getMenuActionsByMenuId(menuId);
    }

    @Override
    public BaseMenu getMenu(long menuId) {
        return baseAuthorityDomainService.getMenu(menuId);
    }

    @Override
    public long addMenu(BaseMenu menu) {
        return baseAuthorityDomainService.addMenu(menu);
    }

    @Override
    public void updateMenu(BaseMenu menu) {
        baseAuthorityDomainService.updateMenu(menu);
        openRestTemplate.refreshGateway();
    }

    @Override
    public void removeMenu(long menuId) {
        baseAuthorityDomainService.removeMenu(menuId);
        openRestTemplate.refreshGateway();
    }
}