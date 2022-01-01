# spring-cloud-lwohvye
Spring Cloud Netflix

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
