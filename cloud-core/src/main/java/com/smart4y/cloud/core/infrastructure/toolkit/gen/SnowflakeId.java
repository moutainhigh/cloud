package com.smart4y.cloud.core.infrastructure.toolkit.gen;

import com.smart4y.cloud.core.infrastructure.spring.SpringContextHolder;
import com.smart4y.cloud.core.infrastructure.toolkit.gen.SnowflakeIdWorker;
import tk.mybatis.mapper.genid.GenId;

/**
 * 自定义主键生成策略
 *
 * @author Youtao
 *         Created by youtao on 2019-04-30.
 */
public class SnowflakeId implements GenId<Long> {

    @Override
    public Long genId(String table, String column) {
        SnowflakeIdWorker worker = SpringContextHolder.getBean(SnowflakeIdWorker.class);
        return worker.nextId();
    }
}