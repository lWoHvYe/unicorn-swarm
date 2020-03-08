package com.lwohvye.springcloud.springcloudlwohvyeprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.config
 * @className AsyncConfig
 * @description 异步执行配置类
 * @date 2020/1/16 14:33
 */
@Configuration
//开启异步
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 8;
    private static final int QUEUE_CAPACITY = 20;
    public static final int KEEP_ALIVE_SECONDS = 200;
    public static final boolean WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN = true;
    public static final int AWAIT_TERMINATION_SECONDS = 60;

    @Bean
    public Executor taskExecutor() {
        // Spring 默认配置是核心线程数大小为1，最大线程容量大小不受限制，队列容量也不受限制。
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数，即同时运行的最大线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);
        // 最大线程数，当队列满时，才会根据该参数创建线程，且保证最终线程数不超过该值，对于无界队列，该参数用不到
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        // 队列大小
        executor.setQueueCapacity(QUEUE_CAPACITY);
        // 线程空闲时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        // 当最大池已满时，此策略保证不会丢失任务请求，但是可能会影响应用程序整体性能。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程名称前缀
        executor.setThreadNamePrefix(" WHY-ThreadPoolTaskExecutor-");
        // 等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(WAIT_FOR_TASKS_TO_COMPLETE_ON_SHUTDOWN);
        // 线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
        executor.initialize();
        return executor;
    }
}
