package com.smart4y.cloud.core.ddd;

import java.util.List;

/**
 * 仓储
 *
 * @author Youtao
 *         Created by youtao on 2019/12/27.
 */
public interface Repository<Entity> {

    void create(Entity entity);

    Entity selectByKey(Object key);

    Entity selectOneBySpecification(Entity example);

    List<Entity> selectAll();

    List<Entity> selectBySpecification(Entity example);

    void update(Entity entity);

    void remove(Entity entity);

    void delete(Entity entity);
}