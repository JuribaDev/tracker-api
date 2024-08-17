package com.juriba.tracker.common.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class Bucket4jConfig {

    @Bean
    public ProxyManager<String> proxyManager() {
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .maximumSize(100);

        return new CaffeineProxyManager<>(
                caffeineBuilder,
                Duration.ofMinutes(5)
        );
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(250).refillIntervally(250, Duration.ofMinutes(2)))
                .build();
    }
}
