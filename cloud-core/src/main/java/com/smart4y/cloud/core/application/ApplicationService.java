package com.smart4y.cloud.core.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用服务注解
 * <p>
 * 事务传播行为：
 * 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；
 * 如果当前没有事务，则创建一个新的事务（则该取值等价于{@link org.springframework.transaction.TransactionDefinition#PROPAGATION_REQUIRED}）。
 * </p>
 *
 * @author Youtao
 *         Created by youtao on 2019-07-05.
 */
@Service
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
public @interface ApplicationService {
}