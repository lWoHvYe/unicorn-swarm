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
- websocket 应该只是消息推送

### 部分笔记

- Provider提供Web接口
- Consumer中Feign使用接口调用映射，当调用失败时进入FallbackFactory
- 将Feign相关放到api层，是因为在provider和consumer都有用到，在provider中通过这种方式可能（？）可以调用到其他的provider实例。这算是一种情况，但Feign是放到consumer层还是下沉到api层，后续再考虑考虑
- Provider和Consumer本身是相对的概念。
    - 放到api层的好处是：api由provider侧维护，当consumer需要使用时，只需引入api即可，不必再自行开发Feign及降级逻辑。
    - 可能带来的问题是：在consumer层引入了些不需要的Feign（违背最小依赖），另一方面api侧对Feign的改动，可能对consumer侧产生影响，有一定的不可控，以及不同的consumer侧可能有自己的降级逻辑（这个自己再写个Feign可以解决）。
- 配置刷新使用bus总线。可以通过config-server触发，也可通过config-client触发，还可定点通知
    - 不使用总线时，可 post 请求 client-host:port/actuator/refresh 来主动刷新配置
    - 使用总线时，可 post 请求 server-host:port/actuator/busrefresh 来刷新配置，这里注意 busrefresh是自己配置的path，不要带横线"-"，也不要有驼峰命名
      之前的bus-refresh与busRefresh都报了405
    - 使用总线时定点通知，可 post 请求 server-host:port/actuator/busrefresh/{destination} ，destination为application-name:port ，
      不带port是同name的所有，同时支持模糊匹配，prefix* 匹配同一prefix的所有（有一个name为springCloudBus的Topic Exchange，所以支持这些路由操作）

#### Feign的两种实现方式

- 第一种就是Feign和生产者的RequestMapping保持一致
- 第二种是在Api模块中定义提供所有请求的接口xxxApi，然后controller实现xxxAPI，FeignClient继承xxxAPI，这样就不必要写重复代码了。

#### TODO

- 升级Spring Cloud到最新版本（Eureka和Hystrix保持维护的最终版）
- 简化业务逻辑，只验证组件
- 使用Api-Gateway替换Zuul做Gateway
