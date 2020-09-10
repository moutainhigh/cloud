# cloud-common-loadbalancer

负载均衡器实现。提供了hash、轮询、加权轮询、平滑加权轮询、随机、加权随机算法。

## 算法规则

|名称|含义|
|:--|--|
|Hash|hash|
|RoundRobin|轮询|
|WeightRoundRobin|加权轮询|
|SmoothWeightRoundRobin|平滑加权轮询|
|Random|随机|
|WeightRandom|加权随机|

## 使用说明

#### 工厂模式

```
ILoadBalancer<String, Object> loadBalancer = LoadBalancerFactory.build("Hash");
loadBalancer.addTargets(Arrays.asList("t1", "t2", "t3"), true);
loadBalancer.choose();
// loadBalancer.choose(new Object());
```

#### 构造器方法

```
ILoadBalancer<String, Object> loadBalancer = new HashLoadBalancer<>();
loadBalancer.addTargets(Arrays.asList("t1", "t2", "t3"), true);
loadBalancer.choose();
// loadBalancer.choose(new Object());
```

#### 设置对象权重 
```
loadBalancer.setWeight(target, 1);
```
