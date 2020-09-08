package com.smart4y.cloud.loadbalancer;

/**
 * 负载均衡算法
 *
 * @author Youtao on 2020/9/8 09:49
 */
public enum LBType {

    /**
     * 哈希
     */
    Hash,
    /**
     * 随机
     */
    Random,
    /**
     * 轮询
     */
    RoundRobin,
    /**
     * 加权随机
     */
    WeightRandom,
    /**
     * 加权轮询
     */
    WeightRoundRobin,
    /**
     * 平滑加权轮询
     */
    SmoothWeightRoundRobin
}