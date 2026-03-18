package com.rebellion.notifyhub.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebellion.notifyhub.entity.Notification;
import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.entity.NotificationPreference;
import com.rebellion.notifyhub.entity.User;
import com.rebellion.notifyhub.entity.enums.NotificationStatus;
import com.rebellion.notifyhub.repository.NotificationRepo;
import com.rebellion.notifyhub.repository.UserRepo;
import com.rebellion.notifyhub.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService{

	private final UserRepo userRepo;
	private final NotificationRepo notificationRepo;
	
	@Override
	public void createNotification(NotificationEvent event) {
		User user = userRepo.findById(event.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
		Notification notification = new Notification(user, "New Event: " + event.getEventType(), event.getPayload(), event.getEventType());
		notificationRepo.save(notification);
	}

	@Override
	@Transactional
	public void processPendingNotifications() {
		List<Notification> notifications = notificationRepo.findByStatus(NotificationStatus.PENDING);
		for (Notification notification : notifications) {
			try {
				processNotification(notification);
				notification.markSent();
			} catch (Exception e) {
				log.info("Unable to send notification to " + notification.getUser().getEmail());
				notification.markFailed();
			}
		}
	}

	// HELPER METHODS
	private void processNotification(Notification notification) {
		NotificationPreference preference = notification.getUser().getPreference();
		if(preference == null){
			throw new EntityNotFoundException("Preference not found");
		}
		if(preference.isEmailEnabled()) sendEmail(notification);
		if(preference.isPushEnabled()) sendPush(notification);
	}

	private void sendEmail(Notification notification) {
		// TODO replace with email service
		log.info(
			"Sending email to {}: {}",
			notification.getUser().getEmail(),
			notification.getMessage()
		);
	}

	private void sendPush(Notification notification) {
		// TODO replace with websocket push
		log.info(
			"Sending push to userId {}: {}",
			notification.getUser().getId(),
			notification.getMessage()
		);
	}
}
