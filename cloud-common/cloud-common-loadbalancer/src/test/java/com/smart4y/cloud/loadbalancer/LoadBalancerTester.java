package com.smart4y.cloud.loadbalancer;

import com.smart4y.cloud.loadbalancer.factory.LoadBalancerFactory;
import com.smart4y.cloud.loadbalancer.wrapper.TargetWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * load balancer tester
 */
public class LoadBalancerTester {

    private final LoadBalancer<String, Object> lb;

    private final List<TargetWrapper<String>> wrappers;

    private final int threadSize;

    private final int threadJobSize;

    public static void main(String[] args) throws Exception {
        int targetSize = 4;
        List<TargetWrapper<String>> wrappers = new ArrayList<>(targetSize);
        for (int i = 1; i <= targetSize; i++) {
            TargetWrapper<String> wrapper = TargetWrapper.of("t" + i);
            wrapper.setActive(true);
            wrappers.add(wrapper);
        }
        int threadSize = 10;
        int threadJobSize = 10;

        for (LBType lbType : LBType.values()) {
            new LoadBalancerTester(LoadBalancerFactory.build(lbType), wrappers, threadSize, threadJobSize).start();
        }
    }

    public LoadBalancerTester(LoadBalancer<String, Object> lb, List<TargetWrapper<String>> wrappers, int threadSize, int threadJobSize) {
        this.lb = lb;
        this.wrappers = wrappers;
        this.threadSize = threadSize;
        this.threadJobSize = threadJobSize;
    }

    public void start() throws Exception {
        Map<String, AtomicInteger> sizeMap = new HashMap<>();

        lb.addTargetWrappers(wrappers);
        int size = wrappers.size();
        for (int i = 0; i < size; i++) {
            String target = wrappers.get(i).getTarget();
            lb.setWeight(target, size - i);
            sizeMap.put(target, new AtomicInteger());
        }

        String lbName = lb.getClass().getSimpleName();

        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        long startTime = System.nanoTime();
        for (int i = 0; i < threadSize; i++) {
            String chooseReferenceObject = "chooseReferenceObject" + i;
            new Thread(() -> {
                for (int j = 0; j < threadJobSize; j++) {
                    String result = lb.choose(chooseReferenceObject);
                    System.out.println(result);
                    sizeMap.get(result).addAndGet(1);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        long endTime = System.nanoTime();
        System.out.println(lbName);
        System.out.println("thread size: " + threadSize);
        System.out.println("thread job size: " + threadJobSize);
        System.out.println("used time: " + (endTime - startTime));
        System.out.println(lbName + " result:");
        sizeMap.entrySet().forEach(System.out::println);
        System.out.println();
    }
}