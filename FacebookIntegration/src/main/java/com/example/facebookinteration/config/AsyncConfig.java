package com.example.facebookinteration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean("webhookTaskExecutor")
    public Executor webhookTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Cấu hình an toàn cho webhook
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Webhook-");
        executor.setRejectedExecutionHandler((r, exec) -> log.warn("Webhook task rejected. Active threads: {}, Queue size: {}", exec.getActiveCount(), exec.getQueue().size()));
        executor.initialize();
        log.info("Webhook async thread pool configured: core={}, max={}, queue={}", executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());
        return executor;
    }

    @Bean("syncTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Cấu hình an toàn cho tác vụ đồng bộ
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("SyncTask-");
        executor.setRejectedExecutionHandler((r, exec) -> log.warn("Sync task rejected. Active threads: {}, Queue size: {}", exec.getActiveCount(), exec.getQueue().size()));
        executor.initialize();
        log.info("Sync task thread pool configured: core={}, max={}, queue={}", executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());
        return executor;
    }
}
