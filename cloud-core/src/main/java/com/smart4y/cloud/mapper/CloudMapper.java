package com.smart4y.cloud.mapper;

import com.smart4y.cloud.mapper.additional.PageMapper;
import com.smart4y.cloud.mapper.additional.SaveOrUpdateMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 自定义 Mapper 接口
 *
 * @author Youtao
 * Created by youtao on 2019-04-30.
 */
public interface CloudMapper<T> extends
        Mapper<T>,
        SaveOrUpdateMapper<T>,
        PageMapper<T> {
}