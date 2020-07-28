package com.smart4y.cloud.mapper;

import com.smart4y.cloud.mapper.SaveOrUpdateMapper;
import com.smart4y.cloud.mapper.BatchMapper;
import com.smart4y.cloud.mapper.PageMapper;
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