package com.crm.tracing;

import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public Sampler alwaysSampler() {
        return Sampler.create(1.0f); // Sample 100% of requests for demo
    }
}