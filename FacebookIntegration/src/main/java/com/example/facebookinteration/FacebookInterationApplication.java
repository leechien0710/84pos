package com.example.facebookinteration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.company.common.apiresponse.ApiResponseAdvice;

import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableRetry
@ComponentScan(basePackages = {"com.example.facebookinteration", "com.company.common"})
public class FacebookInterationApplication {
    public static void main(String[] args) {
        // Sử dụng cấu hình log4j2 local
        SpringApplication.run(FacebookInterationApplication.class, args);
    }
}
