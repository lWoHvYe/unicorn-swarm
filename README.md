# spring-cloud-lwohvye
Spring Cloud Netflix

基于Spring Boot 2.3.x、Spring Cloud H版
### 模块说明

- api 基础Api模块，定义module及部分util
- config-3344 配置中心，与远程交互方，接受发布配置刷新
- config-client-3355 配置中心，集群服务配置中心，与3344交互，启动获取远程配置
- eureka-x 服务注册与发现集群
- provider-config-client 服务提供，整合3355，启动获取远程配置
- consumer-config-client 服务消费，整合3355，启动获取远程配置
- hystrix 服务熔断与限流
- zuul 网关
- websocket 印象中与配置刷新有关，后续确定

### 部分笔记

- Provider提供Web接口
- Consumer中Feign使用接口调用映射，当调用失败时进入FallbackFactory
- 将Feign相关放到api层，是因为在provider和consumer都有用到，在consumer中通过这种方式可能（？）可以调用到其他的consumer实例。这算是一种情况，但Feign是放到provider层还是下沉到api层，后续再考虑考虑
