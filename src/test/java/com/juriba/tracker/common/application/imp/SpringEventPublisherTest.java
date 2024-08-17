package com.juriba.tracker.common.application.imp;

import com.juriba.tracker.common.application.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.verify;

class SpringEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    private SpringEventPublisher springEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        springEventPublisher = new SpringEventPublisher(applicationEventPublisher);
    }

    @Test
    void publish_shouldDelegateToApplicationEventPublisher() {
        // Arrange
        DomainEvent domainEvent = new TestDomainEvent();

        // Act
        springEventPublisher.publish(domainEvent);

        // Assert
        verify(applicationEventPublisher).publishEvent(domainEvent);
    }

    private static class TestDomainEvent implements DomainEvent {
        @Override
        public OffsetDateTime occurredOn() {
            return OffsetDateTime.now();
        }
    }
}