package com.smart4y.cloud.core.infrastructure.mapper;

import com.smart4y.cloud.core.infrastructure.mapper.base.SaveOrUpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义 Mapper 接口
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
public interface CloudMapper<T> extends Mapper<T>, InsertListMapper<T> {

    /**
     * 添加或更新（表必须设置唯一索引并且实体记录中存在索引值，主键Id不为空也可以）
     * <p>
     * 当记录不存在时添加，存在时更新
     * </p>
     *
     * @param record 实体信息
     */
    @InsertProvider(type = SaveOrUpdateProvider.class, method = "dynamicSQL")
    int saveOrUpdate(T record);
}