package com.smart4y.cloud.loadbalancer.factory;

import com.smart4y.cloud.loadbalancer.LoadBalancer;
import com.smart4y.cloud.loadbalancer.LBType;
import com.smart4y.cloud.loadbalancer.impl.AbstractLoadBalancer;
import com.smart4y.cloud.loadbalancer.wrapper.TargetWrapper;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * load balancer factory
 */
public class LoadBalancerFactory {

    /**
     * build load balancer instance by appoint rule
     *
     * @param lbType load balance rule
     * @param <T>    target object class
     * @param <C>    choose reference object
     * @return load balancer instance
     */
    public static <T, C> LoadBalancer<T, C> build(@NonNull LBType lbType) {
        return build(lbType, null);
    }

    /**
     * build load balancer instance by appoint rule
     *
     * @param lbType     load balance rule
     * @param targetList target object list
     * @param <T>        target object class
     * @param <C>        choose reference object
     * @return load balancer instance
     */
    @SuppressWarnings("unchecked")
    public static <T, C> LoadBalancer<T, C> build(@NonNull LBType lbType, List<TargetWrapper<T>> targetList) {
        String instanceClassName = AbstractLoadBalancer.class.getPackage().getName() + "." + lbType.name() + "LoadBalancer";

        try {
            Class<?> clazz = LoadBalancerFactory.class.getClassLoader().loadClass(instanceClassName);

            if (targetList == null) {
                return (LoadBalancer<T, C>) clazz.getConstructor().newInstance();
            } else {
                return (LoadBalancer<T, C>) clazz.getConstructor(List.class).newInstance(targetList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
}