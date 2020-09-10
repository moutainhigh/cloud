package com.smart4y.cloud.core.ddd;

import java.io.Serializable;

/**
 * 实体
 *
 * @author Youtao
 *         Created by youtao on 2019/12/27.
 */
public interface Entity<T> extends Serializable {

    default boolean sameIdentityAs(T other) {
        return this.equals(other);
    }
}