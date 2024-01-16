package com.airbnb.aggregatorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public ExecutorService executorService() {
        // Adjusts its size based on the demand for tasks.
        return Executors.newCachedThreadPool();
    }
}
