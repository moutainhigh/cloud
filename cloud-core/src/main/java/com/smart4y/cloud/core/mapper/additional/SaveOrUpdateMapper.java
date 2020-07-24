package com.smart4y.cloud.core.mapper.additional;

import com.smart4y.cloud.core.mapper.base.SaveOrUpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * 添加或更新Mapper
 *
 * @author Youtao
 * on 2020/7/21 15:35
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SaveOrUpdateMapper<T> {

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