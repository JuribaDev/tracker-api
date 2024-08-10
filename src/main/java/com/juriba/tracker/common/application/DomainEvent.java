package com.juriba.tracker.common.application;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime occurredOn();
}
