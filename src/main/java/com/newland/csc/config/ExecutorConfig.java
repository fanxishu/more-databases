package com.newland.csc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author chenshen
 * @Description com.newland.taas.proxy.async.ExecutorConfig
 * @Date 2017/10/19
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    /** Set the ThreadPoolExecutor's core pool size. */
    private static final int CORE_POOL_SIZE = 10;
    /** Set the ThreadPoolExecutor's maximum pool size. */
    private static final int MAX_POOL_SIZE = 200;
    /** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
    private static final int QUEUE_CAPACITY = 10;

    /**
     * 初始化线程池
     * @return
     */
    @Bean
    public Executor mySimpleAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("MySimpleExecutor-");
        executor.initialize();
        return executor;
    }

}
