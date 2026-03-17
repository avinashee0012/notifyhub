package com.rebellion.notifyhub.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rebellion.notifyhub.dto.request.NotificationEventRequestDto;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.exception.CustomInvalidJsonStructure;
import com.rebellion.notifyhub.messaging.NotificationEventPublisher;
import com.rebellion.notifyhub.repository.NotificationEventRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.NotificationEventService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventServiceImpl implements NotificationEventService{

	private final UserRepo userRepo;
    private final NotificationEventRepo notificationEventRepo;
    private final NotificationEventPublisher notificationEventPublisher;
	private final ObjectMapper objectMapper;

    @Override
    public void ingestEvent(NotificationEventRequestDto request) {
		if(!userRepo.existsById(request.getUserId())) throw new EntityNotFoundException("User not found");
		String payloadString;
		try {
			payloadString = objectMapper.writeValueAsString(request.getPayload());
		} catch (JsonProcessingException e) {
			throw new CustomInvalidJsonStructure("NotificationEventRequestDto");
		}
        NotificationEvent event = new NotificationEvent(request.getEventType(), request.getUserId(), payloadString);
        NotificationEvent savedEvent = notificationEventRepo.save(event);
        notificationEventPublisher.publish(savedEvent);
    }
    
}
