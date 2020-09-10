package com.smart4y.cloud.loadbalancer.impl;

import com.smart4y.cloud.loadbalancer.wrapper.TargetWrapper;

import java.util.List;
import java.util.Random;

/**
 * 随机
 *
 * @param <T> target class
 * @param <C> choose reference object
 */
public class RandomLoadBalancer<T, C> extends AbstractLoadBalancer<T, C> {

    /**
     * instantiation Load Balancer with CopyOnWriteArrayList
     */
    public RandomLoadBalancer() {
        super();
    }

    /**
     * instantiation Load Balancer with appoint list
     *
     * @param targetList target object list
     */
    public RandomLoadBalancer(List<TargetWrapper<T>> targetList) {
        super(targetList);
    }

    @Override
    protected T choose0(List<TargetWrapper<T>> activeTargetList, C chooseReferenceObject) {
        TargetWrapper<T> wrapper = activeTargetList.get(new Random().nextInt(activeTargetList.size()));
        return wrapper == null ? null : wrapper.getTarget();
    }
}