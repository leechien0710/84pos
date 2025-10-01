package com.example.facebookinteration.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Cấu hình ThreadPool cho xử lý bất đồng bộ
 * Tối ưu cho việc xử lý webhook với hiệu năng cao
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    private static final Logger log = LogManager.getLogger(AsyncConfig.class);

    @Override
    @Bean(name = "webhookTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Cấu hình core pool size - số thread luôn sẵn sàng (tối ưu cho webhook)
        executor.setCorePoolSize(20);
        
        // Cấu hình max pool size - số thread tối đa (tăng để xử lý peak load)
        executor.setMaxPoolSize(100);
        
        // Cấu hình queue capacity - kích thước hàng đợi (tăng buffer)
        executor.setQueueCapacity(500);
        
        // Tên prefix cho thread
        executor.setThreadNamePrefix("Webhook-Async-");
        
        // Thời gian thread idle trước khi bị terminate (giây) - tối ưu cho webhook
        executor.setKeepAliveSeconds(120);
        
        // Cho phép core thread bị terminate khi idle (giảm resource usage)
        executor.setAllowCoreThreadTimeOut(true);
        
        // Cấu hình thread pool shutdown gracefully
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        
        // Cấu hình rejection policy khi queue đầy
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.warn("Webhook task rejected due to thread pool exhaustion. " +
                        "Active threads: {}, Queue size: {}, Pool size: {}", 
                        executor.getActiveCount(), 
                        executor.getQueue().size(), 
                        executor.getPoolSize());
                
                // Fallback: chạy trên caller thread
                if (!executor.isShutdown()) {
                    r.run();
                }
            }
        });
        
        // Khởi tạo thread pool
        executor.initialize();
        
        log.info("Webhook async thread pool configured: core={}, max={}, queue={}", 
                executor.getCorePoolSize(), 
                executor.getMaxPoolSize(), 
                executor.getQueueCapacity());
        
        return executor;
    }
}
