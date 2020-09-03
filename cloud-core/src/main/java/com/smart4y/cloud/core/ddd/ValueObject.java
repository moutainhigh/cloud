package com.smart4y.cloud.core.ddd;

import java.io.Serializable;

/**
 * 值对象
 *
 * @author Youtao
 *         Created by youtao on 2019/12/27.
 */
public interface ValueObject<V> extends Serializable {

    default boolean sameValueAs(V other) {
        return this.equals(other);
    }
}