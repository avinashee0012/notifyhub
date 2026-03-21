package com.rebellion.notifyhub.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rebellion.notifyhub.dto.response.NotificationResponseDto;
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
public class NotificationServiceImpl implements NotificationService {

	private final UserRepo userRepo;
	private final NotificationRepo notificationRepo;
	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public void createNotification(NotificationEvent event) {
		User user = userRepo.findById(event.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
		Notification notification = new Notification(user, "New Event: " + event.getEventType(), event.getPayload(), event.getEventType());
		notificationRepo.save(notification);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<NotificationResponseDto> getNotifications(Long userId, int page, int size) {
		if (!userRepo.existsById(userId)) {
			throw new EntityNotFoundException("User not found");
		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return notificationRepo.findByUserId(userId, pageable).map(this::toResponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<NotificationResponseDto> getUnreadNotifications(Long userId, int page, int size) {
		if (!userRepo.existsById(userId)) {
			throw new EntityNotFoundException("User not found");
		}
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return notificationRepo.findByUserIdAndStatus(userId, NotificationStatus.SENT, pageable).map(this::toResponseDto);
	}

	@Override
	@Transactional
	public NotificationResponseDto markAsRead(Long notificationId) {
		Notification notification = notificationRepo.findById(notificationId).orElseThrow(() -> new EntityNotFoundException("Notification not found"));
		notification.markRead();
		return toResponseDto(notification);
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
				log.error("Processing failed, rety will handle it");
			}
		}
	}

	// HELPER METHODS
	private void processNotification(Notification notification) {
		NotificationPreference preference = notification.getUser().getPreference();
		if (preference == null) {
			throw new EntityNotFoundException("Preference not found");
		}
		if (preference.isEmailEnabled())
			sendEmail(notification);
		if (preference.isPushEnabled())
			sendPush(notification);
	}

	@Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	private void sendEmail(Notification notification) {
		// TODO can be replaced to email service
		log.info("Sending email to {}: {}", notification.getUser().getEmail(), notification.getMessage());
		// simulate failure for testing
		if (Math.random() < 0.5)
			throw new RuntimeException("Email failed");
	}

	@Recover
	private void recoverEmail(Exception e, Notification notification) {
		log.error("Email permanently failed for {}", notification.getUser().getEmail());
		notification.markFailed();
	}

	private void sendPush(Notification notification) {
		NotificationResponseDto response = toResponseDto(notification);
		messagingTemplate.convertAndSend("/topic/notifications/" + notification.getUser().getId(), response);
	}

	private NotificationResponseDto toResponseDto(Notification notification) {
		return new NotificationResponseDto(notification.getId(), notification.getUser().getId(), notification.getTitle(), notification.getMessage(), notification.getType(), notification.getStatus(), notification.getCreatedAt());
	}
}
