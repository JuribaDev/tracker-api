package com.juriba.tracker.common.domain;

import com.juriba.tracker.common.application.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
