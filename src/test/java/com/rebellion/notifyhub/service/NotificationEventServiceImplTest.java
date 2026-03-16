package com.rebellion.notifyhub.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.messaging.NotificationEventPublisher;
import com.rebellion.notifyhub.repository.NotificationEventRepo;
import com.rebellion.notifyhub.service.impl.NotificationEventServiceImpl;

@ExtendWith(MockitoExtension.class)
public class NotificationEventServiceImplTest {

    @Mock
    private NotificationEventRepo notificationEventRepo;

    @Mock
    private NotificationEventPublisher notificationEventPublisher;

    @InjectMocks
    private NotificationEventServiceImpl notificationEventService;

    @Captor
    private ArgumentCaptor<NotificationEvent> eventCaptor;

    private NotificationEventRequestDto request;

    @BeforeEach
    void setup() {
        request = new NotificationEventRequestDto("JOB_SHORTLISTED", "{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}");
    }

    @Test
    void shouldCreateNotificationEventFromRequest() {
        notificationEventService.ingestEvent(request);

        verify(notificationEventRepo).save(eventCaptor.capture());

        NotificationEvent event = eventCaptor.getValue();

        assertEquals("JOB_SHORTLISTED", event.getEventType());
        assertEquals("{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}", event.getPayload());
    }

    @Test
    void shouldSaveEventToRepository() {
        NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", "{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}");

        when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

        notificationEventService.ingestEvent(request);

        verify(notificationEventRepo, times(1)).save(any(NotificationEvent.class));
    }

    @Test
    void shouldPublishSavedEvent() {
        NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", "{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}");

        when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

        notificationEventService.ingestEvent(request);

        verify(notificationEventPublisher).publish(savedEvent);
    }

    @Test
    void shouldCallSaveBeforePublish() {
        NotificationEvent savedEvent = new NotificationEvent("JOB_SHORTLISTED", "{\"userId\":12,\"jobTitle\":\"Backend Engineer\"}");

        when(notificationEventRepo.save(any(NotificationEvent.class))).thenReturn(savedEvent);

        notificationEventService.ingestEvent(request);

        InOrder inOrder = inOrder(notificationEventRepo, notificationEventPublisher);

        inOrder.verify(notificationEventRepo).save(any(NotificationEvent.class));
        inOrder.verify(notificationEventPublisher).publish(savedEvent);
    }
}