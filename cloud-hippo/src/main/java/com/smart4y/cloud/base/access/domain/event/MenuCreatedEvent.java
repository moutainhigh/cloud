package com.smart4y.cloud.base.access.domain.event;

import lombok.AllArgsConstructor;

/**
 * @author Youtao on 2020/8/12 17:07
 */
@AllArgsConstructor
public class MenuCreatedEvent {

    public long menuId;
    public String menuCode;
}