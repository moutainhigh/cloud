package com.smart4y.cloud.base.access.application.eventhandler;

import com.smart4y.cloud.base.access.application.PrivilegeApplicationService;
import com.smart4y.cloud.base.access.domain.event.*;
import com.smart4y.cloud.core.security.http.OpenRestTemplate;
import com.smart4y.cloud.core.toolkit.Kit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限改变事件处理器
 * <p>
 * 包含：菜单、操作、按钮等的增删改都将引起权限的变化
 * </p>
 *
 * @author Youtao on 2020/8/12 17:02
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class PrivilegeChangedEventHandler {

    private final OpenRestTemplate openRestTemplate;
    private final PrivilegeApplicationService privilegeApplicationService;

    @Autowired
    public PrivilegeChangedEventHandler(OpenRestTemplate openRestTemplate, PrivilegeApplicationService privilegeApplicationService) {
        this.openRestTemplate = openRestTemplate;
        this.privilegeApplicationService = privilegeApplicationService;
    }

    @EventListener
    public void menuCreated(MenuCreatedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));

        privilegeApplicationService.addPrivilegeMenus(event.menuId, event.menuCode);

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void menuParentChanged(MenuParentChangedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // 无需修改权限
        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void menuCodeChanged(MenuCodeChangedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));

        privilegeApplicationService.modifyPrivilegeMenus(event.menuId, event.menuCode, event.oldMenuCode);

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void menuRemoved(MenuRemovedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));

        privilegeApplicationService.removePrivilegesByMenus(event.menuId);

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void elementCreated(ElementRemovedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void elementRemoved(ElementRemovedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void operationCreated(OperationCreatedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void operationRemoved(OperationRemovedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void operationStateChanged(OperationStateChangedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void operationAuthChanged(OperationAuthChangedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }

    @EventListener
    public void operationOpenChanged(OperationOpenChangedEvent event) {
        log.info("---------- > {}: {}", event.getClass().getSimpleName(), Kit.help().json().toJson(event));
        // TODO 更新权限

        openRestTemplate.refreshGateway();
    }
}