package com.smart4y.cloud.base.access.domain.event;

import lombok.AllArgsConstructor;

/**
 * 移除
 *
 * @author Youtao on 2020/8/12 17:07
 */
@AllArgsConstructor
public class OperationRemovedEvent {

    private long operationId;
}