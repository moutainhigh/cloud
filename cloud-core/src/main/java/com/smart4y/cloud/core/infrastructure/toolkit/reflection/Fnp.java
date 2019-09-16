package com.smart4y.cloud.core.infrastructure.toolkit.reflection;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 对象的 方法引用
 *
 * @author Youtao
 *         Created by youtao on 2018/11/28.
 */
public interface Fnp<P> extends Consumer<P>, Serializable {
}