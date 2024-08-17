package com.juriba.tracker.common.application.imp;

import com.juriba.tracker.common.application.RateLimitingService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

@Service
public class RateLimitingServiceImp implements RateLimitingService {

    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfiguration;

    public RateLimitingServiceImp(ProxyManager<String> proxyManager, BucketConfiguration bucketConfiguration) {
        this.proxyManager = proxyManager;
        this.bucketConfiguration = bucketConfiguration;
    }
    @Override
    public ConsumptionProbe tryConsume(String endPoint) {
        Bucket bucket = proxyManager.builder().build(endPoint, () -> bucketConfiguration);
        return bucket.tryConsumeAndReturnRemaining(1);
    }
}
