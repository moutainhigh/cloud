package com.smart4y.cloud.loadbalancer.factory;

import com.smart4y.cloud.loadbalancer.ILoadBalancer;
import com.smart4y.cloud.loadbalancer.wrapper.TargetWrapper;

import java.util.List;

/**
 * load balancer factory
 */
public class LoadBalancerFactory {

    /**
     * build load balancer instance by appoint rule
     *
     * @param rule load balance rule
     * @param <T>  target object class
     * @param <C>  choose reference object
     * @return load balancer instance
     */
    public static <T, C> ILoadBalancer<T, C> build(String rule) {
        return build(rule, null);
    }

    /**
     * build load balancer instance by appoint rule
     *
     * @param rule       load balance rule
     * @param targetList target object list
     * @param <T>        target object class
     * @param <C>        choose reference object
     * @return load balancer instance
     */
    @SuppressWarnings("unchecked")
    public static <T, C> ILoadBalancer<T, C> build(String rule, List<TargetWrapper<T>> targetList) {
        String instanceClassName = "com.smart4y.cloud.loadbalancer.impl." + rule + "LoadBalancer";

        try {
            Class<?> clazz = LoadBalancerFactory.class.getClassLoader().loadClass(instanceClassName);

            if (targetList == null) {
                return (ILoadBalancer<T, C>) clazz.getConstructor().newInstance();
            } else {
                return (ILoadBalancer<T, C>) clazz.getConstructor(List.class).newInstance(targetList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
}