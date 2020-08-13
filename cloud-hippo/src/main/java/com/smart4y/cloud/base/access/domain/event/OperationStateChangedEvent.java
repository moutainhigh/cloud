package com.smart4y.cloud.base.access.domain.event;

import lombok.AllArgsConstructor;

/**
 * 状态改变
 *
 * @author Youtao on 2020/8/12 17:07
 */
@AllArgsConstructor
public class OperationStateChangedEvent {

    private long operationId;
}