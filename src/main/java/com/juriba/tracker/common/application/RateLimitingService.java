package com.juriba.tracker.common.application;

import io.github.bucket4j.ConsumptionProbe;

public interface RateLimitingService {
     ConsumptionProbe tryConsume(String endPoint);
}
