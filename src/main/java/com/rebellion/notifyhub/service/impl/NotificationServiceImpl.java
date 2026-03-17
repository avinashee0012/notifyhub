package com.rebellion.notifyhub.service.impl;

import org.springframework.stereotype.Service;

import com.rebellion.notifyhub.entity.Notification;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.entity.User;
import com.rebellion.notifyhub.repository.NotificationRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

	private final UserRepo userRepo;
	private final NotificationRepo notificationRepo;
	
	@Override
	public void createNotification(NotificationEvent event) {
		User user = userRepo.findById(event.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
		Notification notification = new Notification(user, "New Event: " + event.getEventType(), event.getPayload(), event.getEventType());
		notificationRepo.save(notification);
	}
}
