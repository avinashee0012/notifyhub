package com.rebellion.notifyhub.service.impl;

import org.springframework.stereotype.Service;

import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.messaging.NotificationEventPublisher;
import com.rebellion.notifyhub.repository.NotificationEventRepo;
import com.rebellion.notifyhub.service.NotificationEventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventServiceImpl implements NotificationEventService{

    private final NotificationEventRepo notificationEventRepo;
    private final NotificationEventPublisher notificationEventPublisher;

    @Override
    public void ingestEvent(NotificationEventRequestDto request) {
        NotificationEvent event = new NotificationEvent(request.getEventType(), request.getPayload());
        NotificationEvent savedEvent = notificationEventRepo.save(event);
        notificationEventPublisher.publish(savedEvent);
    }
    
}
