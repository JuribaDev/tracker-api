package com.juriba.tracker.common.application;

import java.time.OffsetDateTime;

public interface DomainEvent {
    OffsetDateTime occurredOn();
}
