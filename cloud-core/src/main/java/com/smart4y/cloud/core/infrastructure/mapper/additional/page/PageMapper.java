package com.smart4y.cloud.core.infrastructure.mapper.additional.page;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smart4y.cloud.core.domain.page.Page;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;

import java.util.List;

/**
 * 分页Mapper
 *
 * @author Youtao
 * on 2020/7/21 15:31
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface PageMapper<T> extends SelectByExampleMapper<T> {

    /**
     * 分页查询
     *
     * @param example  查询对象
     * @param pageNo   当前页
     * @param pageSize 每页条数
     * @param count    是否统计总行数
     * @return 分页对象
     */
    default Page<T> page(Object example, int pageNo, int pageSize, boolean count) {
        PageInfo<T> pageInfo = this.pageInfo(example, pageNo, pageSize, count);
        Page<T> page = new Page<>();
        page.setRecords(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    /**
     * 分页查询
     *
     * @param example  查询对象
     * @param pageNo   当前页
     * @param pageSize 每页条数
     * @param count    是否统计总行数
     * @return 分页对象
     */
    default PageInfo<T> pageInfo(Object example, int pageNo, int pageSize, boolean count) {
        PageHelper.startPage(pageNo, pageSize, count);
        List<T> ts = this.selectByExample(example);
        return new PageInfo<>(ts);
    }
}