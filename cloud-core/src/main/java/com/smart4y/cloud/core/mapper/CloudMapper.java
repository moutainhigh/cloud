package com.smart4y.cloud.core.mapper;

import com.smart4y.cloud.core.mapper.additional.SaveOrUpdateMapper;
import com.smart4y.cloud.core.mapper.additional.BatchMapper;
import com.smart4y.cloud.core.mapper.additional.PageMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义 Mapper 接口
 * <br/>
 * 封装以下方法
 * <p>
 * 批量插入
 * 新增或更新
 * 分页
 * 批量更新
 * </p>
 *
 * @author Youtao
 * Created by youtao on 2019-04-30.
 */
public interface CloudMapper<T> extends
        Mapper<T>,
        SaveOrUpdateMapper<T>,
        PageMapper<T>,
        BatchMapper<T> {
}