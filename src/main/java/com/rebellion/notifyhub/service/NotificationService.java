package com.rebellion.notifyhub.service;

import com.rebellion.notifyhub.entity.NotificationEvent;
import com.rebellion.notifyhub.dto.response.NotificationResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
	void createNotification(NotificationEvent event);

    void processPendingNotifications();

	Page<NotificationResponseDto> getNotifications(Long userId, Pageable pageable);

	Page<NotificationResponseDto> getUnreadNotifications(Long userId, Pageable pageable);

	NotificationResponseDto markAsRead(Long notificationId);
}
