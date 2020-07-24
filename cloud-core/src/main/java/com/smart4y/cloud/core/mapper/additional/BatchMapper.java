package com.smart4y.cloud.core.mapper.additional;

import java.util.List;

/**
 * 批量操作Mapper
 *
 * @author Youtao
 * on 2020/7/21 15:37
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface BatchMapper<T> {

    default void insertBatch(List<? extends T> entities) {
        // TODO 待开发
    }

    default void updateSelectiveBatch(List<? extends T> entities) {
        // TODO 待开发
    }

    default void saveOrUpdateBatch(List<? extends T> entities) {
        // TODO 待开发
    }
}